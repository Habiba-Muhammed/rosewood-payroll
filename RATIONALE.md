# Rosewood Payroll — Refactoring Rationale

## 1. Refactoring Decisions

The inherited `PayrollCalculator` combined employee lookup, gross pay and overtime calculation, tax-band selection, and console output. This made the class difficult to test or extend.

I retained the existing `EmployeeRepository` interface and `InMemoryEmployeeRepository` implementation while separating the calculation responsibilities:

- **`PayrollCalculator`** coordinates employee lookup, gross pay, tax, and the final net-pay result. It no longer prints to the console.
- **`GrossPayCalculator`** handles regular pay and overtime, using named constants for the 40-hour threshold and overtime premium.
- **`TaxCalculator`** selects an applicable tax rule and calculates the deduction.
- **`TaxRule` and `PercentageTaxRule`** separate tax-rule selection from the details of percentage-based tax bands.

All three collaborators are supplied to `PayrollCalculator` through constructor injection rather than created internally.

These changes address the **Single Responsibility Principle (SRP)** and **Dependency Inversion Principle (DIP)** while making the components easier to test independently.

## 2. Extending Tax Rules Without Rewriting Existing Logic

The original implementation embedded the 5%, 15%, and 25% tax bands directly in `PayrollCalculator` using `if-else` statements.

The refactored design uses a collection of `TaxRule` implementations. A new percentage band can be introduced by updating the configured rule ranges, while a different calculation method can be introduced through another implementation of `TaxRule`.

Neither change requires modifying the rule-selection algorithm in `TaxCalculator` or the net-pay calculation workflow in `PayrollCalculator`.

This supports the **Open/Closed Principle (OCP)**.

The original tax calculation behaviour—applying one percentage to the entire gross pay—was preserved for valid inputs. The implementation does not assume progressive taxation.

## 3. Exception Handling

The original implementation caught all exceptions and returned `0`, potentially making failed calculations appear to be legitimate zero-pay results.

I removed this broad exception handler and introduced `EmployeeNotFoundException` for missing employees.

This represents an **intentional behaviour change** rather than a purely structural refactor.

Additional validation was introduced to:

- Reject negative and non-finite hours and hourly rates.
- Detect numeric overflow during gross pay calculations.
- Reject invalid tax rates and band boundaries.
- Detect missing or overlapping tax rules.
- Reject negative, non-finite, or excessive calculated tax amounts.

Exceptions now propagate to the caller rather than being silently suppressed.

## 4. Evaluation of AI-Generated Code and Tests

I retained useful parts of the inherited implementation, including the repository abstraction and the original calculation rules where appropriate.

However, the original AI-generated tests contained several weaknesses:

- **Hollow test:** `calculatesOvertimePay()` mocked `PayrollCalculator` itself, meaning the real calculation was never executed. This was replaced with a test using the actual calculator.
- **Tautological assertion:** `returnsPayForAnEmployee()` used `assertNotNull()` on a primitive `double`. This was replaced with an exact expected-value assertion.
- **Weak assertion:** `calculatesNetPay()` only checked whether the result was positive. This was replaced with an assertion verifying the expected net pay.

Characterization tests were used to document important calculation results before refactoring.

Additional JUnit 5 tests cover overtime, tax-band boundaries, invalid inputs, and exception handling. Mockito tests verify the behaviour of injected dependencies, while integration tests exercise the actual components together.

## 5. Additional Improvement Beyond the Brief

I implemented **ambiguous tax-rule detection** to prevent multiple tax rules from applying silently to the same gross pay.

Previously, `TaxCalculator` would return the result from the first matching tax rule.

The updated implementation checks whether more than one rule matches and throws an `IllegalStateException` when an overlap is detected.

This improvement is particularly valuable because Rosewood plans to introduce additional tax bands. Without this safeguard, overlapping ranges could cause incorrect deductions depending on the order of the configured rules.

A dedicated test verifies this behaviour.

The safeguard detects overlaps when calculating a particular amount, rather than validating every possible overlap during configuration.

## 6. Test Effectiveness

JaCoCo confirmed that the tests exercise every branch in the payroll implementation, while PIT provided additional evidence of whether those tests could detect incorrect behaviour.

Mutation testing initially exposed weaknesses around boundary values, including zero hourly rates, zero gross pay, and tax rates at their permitted limits. Adding targeted tests improved mutation coverage and test strength.

The remaining surviving covered mutation changes the overtime condition from `> 40` to `>= 40`. Since the overtime premium is zero at exactly 40 hours, this mutation does not change the observable result and is considered equivalent for the current implementation.

This demonstrates why coverage percentages alone are insufficient: tests must also verify meaningful outcomes and detect incorrect calculations.

Detailed JaCoCo and PIT results are provided in the [README](README.md).
