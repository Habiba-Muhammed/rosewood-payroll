# Rosewood Payroll — Refactoring Rationale

## 1. Refactoring Decisions

The inherited `PayrollCalculator` combined employee lookup, gross pay and overtime calculation, tax-band selection, and console output. This made the class difficult to test or extend.

I retained the existing `EmployeeRepository` interface and `InMemoryEmployeeRepository` implementation while separating the calculation responsibilities:

- **`PayrollCalculator`** coordinates employee lookup, gross pay, tax deductions, and the final net-pay calculation. It no longer prints to the console.
- **`GrossPayCalculator`** handles regular pay and overtime using named constants for the 40-hour threshold and overtime multiplier.
- **`TaxCalculator`** selects the applicable tax rule and calculates deductions.
- **`TaxRule` and `PercentageTaxRule`** separate tax-rule selection from the implementation of percentage-based tax calculations.
- **`TaxRuleConfig`** centralizes the default tax bands, eliminating duplicated configurations across the application and tests.
- **`Money`** provides a consistent monetary rounding policy using `BigDecimal`.

The employee repository, gross pay calculator, and tax calculator are supplied to `PayrollCalculator` through constructor injection rather than instantiated internally.

These changes improve adherence to the **Single Responsibility Principle (SRP)** and **Dependency Inversion Principle (DIP)** while making components easier to test independently.

However, `PayrollCalculator` still depends on the concrete `GrossPayCalculator` and `TaxCalculator` classes. Introducing interfaces for these collaborators would further improve dependency inversion.

## 2. Extending Tax Rules Without Rewriting Existing Logic

The original implementation embedded the 5%, 15%, and 25% tax bands directly in `PayrollCalculator` using `if-else` statements.

The refactored design uses a collection of `TaxRule` implementations. The default tax bands are now centralized in `TaxRuleConfig`, allowing changes to the configured bands without modifying `PayrollCalculator` or the rule-selection algorithm in `TaxCalculator`.

A different tax calculation method can also be introduced through another implementation of `TaxRule`.

This supports the **Open/Closed Principle (OCP)** by allowing the system to accommodate new tax rules without modifying its core calculation workflow.

The original tax calculation behaviour—applying one percentage to the entire gross pay—was preserved for valid inputs. The implementation does not assume progressive taxation.

## 3. Exception Handling and Monetary Precision

The original implementation caught all exceptions and returned `0`, potentially making failed calculations appear to be legitimate zero-pay results.

I removed this broad exception handler and introduced `EmployeeNotFoundException` for missing employees.

This represents an **intentional behaviour change** rather than a purely structural refactor.

Additional validation was introduced to:

- Reject negative and non-finite inputs to legacy `double` calculation methods.
- Reject null and negative `BigDecimal` hours, hourly rates, and gross pay.
- Detect numeric overflow when converting results through legacy `double` methods.
- Reject invalid tax rates and tax-band boundaries.
- Detect missing or overlapping tax rules.
- Reject negative, non-finite, null, or excessive calculated tax amounts, as applicable to each calculation path.

Exceptions now propagate to callers rather than being silently suppressed. The `Demo` class explicitly catches `EmployeeNotFoundException` when demonstrating an unknown employee.

### Monetary Precision Improvement

The initial implementation used `double` for monetary calculations without an explicit rounding policy.

To improve monetary precision, I introduced `BigDecimal` as the primary calculation path and added the `Money` utility class to provide consistent rounding.

The application now uses:

- Two decimal places for monetary amounts.
- `RoundingMode.HALF_UP` for rounding.
- Rounded gross pay and tax deductions before calculating net pay.

The overtime calculation was also revised to separate regular hours from overtime hours using `min()` and subtraction. This makes the behaviour at exactly 40 hours explicit.

The original `double` methods remain available for backward compatibility, but the preferred monetary calculation path uses `BigDecimal`.

## 4. Evaluation of AI-Generated Code and Tests

I retained useful parts of the inherited implementation, including the repository abstraction and the original calculation rules where appropriate.

However, the original AI-generated tests contained several weaknesses:

- **Hollow test:** `calculatesOvertimePay()` mocked `PayrollCalculator` itself, meaning the real calculation was never executed. This was replaced with a test using the actual calculator.
- **Tautological assertion:** `returnsPayForAnEmployee()` used `assertNotNull()` on a primitive `double`. This was replaced with an exact expected-value assertion.
- **Weak assertion:** `calculatesNetPay()` only checked whether the result was positive. This was replaced with an assertion verifying the expected net pay.

Characterization tests were used to document important calculation results before refactoring.

Additional JUnit 5 tests cover regular pay, overtime, tax-band boundaries, invalid inputs, monetary precision, and exception handling.

Mockito tests verify the behaviour of injected dependencies, while integration tests exercise the actual components together.

I also introduced `PayrollTestFixtures` to centralize common test setup and eliminate repeated dependency initialization.

Following the `BigDecimal` refactoring, additional tests were implemented to verify:

- Null and negative decimal inputs.
- Invalid monetary amounts.
- Missing and overlapping tax rules.
- Invalid calculated decimal tax amounts.
- Exact tax-band boundaries.
- Monetary rounding behaviour.

These changes improved the tests' ability to detect meaningful calculation errors.

## 5. Additional Improvement Beyond the Brief

I implemented **ambiguous tax-rule detection** to prevent multiple tax rules from applying silently to the same gross pay.

Previously, `TaxCalculator` would return the result from the first matching tax rule.

The updated implementation checks whether more than one rule matches and throws an `IllegalStateException` when an overlap is detected.

This improvement is particularly valuable because Rosewood plans to introduce additional tax bands. Without this safeguard, overlapping ranges could cause incorrect deductions depending on the order of the configured rules.

Dedicated tests verify this behaviour for both the legacy `double` and new `BigDecimal` calculation paths.

The safeguard detects overlaps when calculating a particular amount, rather than validating every possible overlap during configuration.

## 6. Test Effectiveness

JaCoCo was used to measure line and branch coverage, while PIT mutation testing evaluated whether the tests could detect changes that introduce incorrect behaviour.

### JaCoCo Results

The latest JaCoCo report produced the following results:

| Metric | Result |
|---|---|
| Overall line coverage | 84.36% (151/179) |
| Overall branch coverage | 89.81% (97/108) |
| `GrossPayCalculator` | 100% line and branch coverage |
| `TaxCalculator` | 100% line and branch coverage |
| `Money` | 100% line and branch coverage |

The additional `BigDecimal` validation tests improved overall line coverage from 79.89% to 84.36% and branch coverage from 79.63% to 89.81%.

The three critical calculation and validation classes achieved 100% line and branch coverage in the latest report.

### PIT Mutation Testing Results

The latest PIT mutation-testing report produced the following results:

| Metric | Result |
|---|---|
| Mutation coverage | 82% (76/93) |
| Test strength | 92% (76/83) |
| Killed mutations | 76 |
| Surviving covered mutations | 7 |
| Mutations without test coverage | 10 |

The latest report shows 100% mutation coverage for `GrossPayCalculator`, `PayrollCalculator`, and `Money`.

Seven covered mutations remain alive in `PercentageTaxRule` and `TaxCalculator`, primarily involving conditional boundaries.

These mutations indicate opportunities for further testing. Each surviving mutation should be examined to determine whether it represents a genuine testing weakness or an equivalent mutation.

Detailed JaCoCo and PIT results are provided in the [README](README.md).
