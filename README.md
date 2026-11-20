# Rosewood Payroll — Net Pay Service

## Overview

Rosewood Payroll is a Java-based service that calculates employee net pay based on hours worked, hourly rates, overtime, and applicable tax bands.

This project was developed as part of the **AmaliTech Code Quality and Testing Applied Lab**.

The initial implementation was generated using an AI assistant and contained several code smells, SOLID violations, inadequate exception handling, and ineffective tests.

The service has been refactored to improve maintainability, extensibility, reliability, and testability using Clean Code principles, SOLID, JUnit 5, and Mockito.

The primary monetary calculation path now uses `BigDecimal` with an explicit rounding policy to improve monetary precision.

## Project Architecture

The refactored application separates responsibilities across the following components:

| Component | Responsibility |
|---|---|
| `Employee` | Represents employee information |
| `EmployeeRepository` | Defines the employee retrieval contract |
| `InMemoryEmployeeRepository` | Provides an in-memory employee repository |
| `PayrollCalculator` | Coordinates the net pay calculation |
| `GrossPayCalculator` | Calculates regular and overtime gross pay |
| `TaxRule` | Defines a common interface for tax rules |
| `PercentageTaxRule` | Implements percentage-based tax bands |
| `TaxCalculator` | Selects applicable tax rules and calculates deductions |
| `TaxRuleConfig` | Centralizes the default tax-band configuration |
| `Money` | Provides monetary conversion and rounding utilities |
| `EmployeeNotFoundException` | Reports missing employee errors |
| `Demo` | Demonstrates payroll calculations |
| `PayrollTestFixtures` | Provides reusable dependency setup for tests |

`PayrollTestFixtures` is located under `src/test/java` and is not part of the production application.

### SOLID Principles Applied

**Single Responsibility Principle (SRP):** Gross pay calculations, tax calculations, employee retrieval, and monetary rounding are handled by separate components.

**Open/Closed Principle (OCP):** The `TaxRule` abstraction allows new tax rules to be introduced without modifying the existing tax calculator logic.

**Dependency Inversion Principle (DIP):** Dependencies are supplied through constructor injection rather than instantiated directly inside `PayrollCalculator`.

`PayrollCalculator` depends on the `EmployeeRepository` interface, although `GrossPayCalculator` and `TaxCalculator` remain concrete dependencies. Introducing interfaces for these collaborators would further improve dependency inversion.

## Tax Configuration

Tax bands are centralized in `TaxRuleConfig.java` instead of being duplicated across `Demo.java` and multiple test classes.

The application currently defines the following tax bands:

| Gross Pay | Tax Rate |
|---|---|
| 0 to less than 1,000 | 5% |
| 1,000 to less than 5,000 | 15% |
| 5,000 and above | 25% |

Each tax band includes its lower boundary and excludes its upper boundary.

The tax rules use a single percentage determined by the applicable gross-pay band, rather than progressive taxation.

New tax bands can be introduced by updating `TaxRuleConfig.java` without changing the existing calculation logic.

The configuration is centralized in Java rather than loaded from an external configuration file.

## Monetary Precision and Rounding

The application now uses `BigDecimal` for its primary monetary calculation path instead of relying entirely on floating-point `double` values.

### Rounding Policy

The application uses:

- Two decimal places for monetary amounts.
- `RoundingMode.HALF_UP` for rounding.
- A shared `Money` utility class to apply the rounding policy consistently.

### Calculation Process

1. Calculate regular pay for up to 40 hours.
2. Calculate overtime pay for additional hours at 1.5 times the hourly rate.
3. Add regular and overtime pay to obtain gross pay.
4. Round gross pay to two decimal places.
5. Select the applicable tax band.
6. Calculate and round the tax deduction.
7. Subtract tax from gross pay to obtain net pay.

For example, an employee working 40 hours at an hourly rate of 20 receives:

| Calculation | Amount |
|---|---:|
| Regular pay | 800.00 |
| Overtime pay | 0.00 |
| Gross pay | 800.00 |
| Tax (5%) | 40.00 |
| Net pay | 760.00 |

The overtime calculation now separates regular hours from overtime hours using `min()` and subtraction.

This makes the 40-hour boundary explicit and avoids the earlier equivalent mutation involving `> 40` and `>= 40`.

### Backward Compatibility

The existing `double` calculation methods remain available for compatibility with earlier tests and callers.

However, the preferred calculation path uses `BigDecimal` to provide more predictable monetary precision.

The retained `double` overloads and default `TaxRule` adapters are compatibility mechanisms and do not provide the same end-to-end precision guarantees as the decimal-based path.

## Exception Handling

The application validates payroll inputs and reports failures through meaningful exceptions.

- Missing employees throw `EmployeeNotFoundException`.
- Negative or non-finite inputs to legacy `double` methods are rejected.
- Null or negative `BigDecimal` hours, hourly rates, and gross amounts are rejected.
- Invalid tax-band configurations are rejected.
- Missing or overlapping tax rules trigger errors.
- Invalid calculated tax amounts are rejected.
- Tax amounts greater than gross pay are rejected.
- Null tax results in the decimal calculation path are rejected.

Exceptions are propagated rather than silently converted into zero pay.

The `Demo` class now catches `EmployeeNotFoundException` when demonstrating an unknown employee, preventing an uncaught exception from terminating the example unexpectedly.

## Running the Application

### Requirements

- JDK 21
- Apache Maven

### Run the Demo

```bash
mvn compile
mvn exec:java -Dexec.mainClass=com.rosewood.payroll.Demo
```

Expected output:

```text
Result e1, 40h @ 20     -> 760.00
Result e2, 50h @ 30     -> 1402.50
Payroll error: Employee not found: unknown
```

The final error message is printed to standard error.

### Run All Tests

```bash
mvn test
```

### Generate JaCoCo Coverage Report

```bash
mvn clean verify
```

Open:

`target/site/jacoco/index.html`

### Run PIT Mutation Testing

```bash
mvn test-compile org.pitest:pitest-maven:mutationCoverage
```

Open:

`target/pit-reports/index.html`

## Testing Strategy

The project uses JUnit 5 and Mockito to verify payroll calculations, monetary precision, and failure behaviour.

### Unit Tests

Unit tests verify individual components, including:

- Regular and overtime pay calculations.
- Tax calculations and tax-band boundaries.
- Invalid and missing tax-rule configurations.
- Negative, null, and non-finite inputs.
- Monetary rounding using `BigDecimal`.
- Invalid calculated tax amounts.
- Employee lookup and exception handling.

Additional tests were introduced to validate the new `BigDecimal` calculation paths.

These include null and negative decimal inputs, missing and overlapping tax rules, invalid tax results, and precise boundary calculations.

### Mockito Tests

Mockito tests isolate `PayrollCalculator` from its dependencies.

They verify the behaviour of the payroll service when dependencies return expected values or produce exceptional conditions.

### Integration Tests

Integration tests verify that the real employee repository, gross pay calculator, tax calculator, and tax rules work together correctly.

The `PayrollTestFixtures` class provides shared test setup to reduce duplication.

Tests requiring unusual configurations continue to create their own tax rules explicitly.

### Characterization Tests

Characterization tests preserve important behaviour from the original implementation.

They verify expected payroll results before and after refactoring to help detect unintended behavioural changes.

The original AI-generated tests were evaluated and improved to verify actual results rather than relying on weak assertions or configured mock responses.

## Code Coverage and Mutation Testing

### JaCoCo Code Coverage Results

The latest JaCoCo results were generated after the `BigDecimal` refactoring and the addition of targeted validation tests.

| Metric | Result |
|---|---:|
| Overall line coverage | **84.36% (151/179)** |
| Overall branch coverage | **89.81% (97/108)** |
| `GrossPayCalculator` line coverage | **100%** |
| `GrossPayCalculator` branch coverage | **100%** |
| `TaxCalculator` line coverage | **100%** |
| `TaxCalculator` branch coverage | **100%** |
| `Money` line coverage | **100%** |
| `Money` branch coverage | **100%** |

The additional validation tests improved overall line coverage from 79.89% to 84.36% and branch coverage from 79.63% to 89.81%.

The three critical calculation and validation classes — `GrossPayCalculator`, `TaxCalculator`, and `Money` — now achieve 100% line and branch coverage.

The overall coverage figures include demonstration code, simple data-access methods, and compatibility paths that are not fully exercised by the automated tests.

The objective is to verify meaningful behaviour rather than introduce artificial tests solely to increase coverage percentages.

### PIT Mutation Testing Results

The latest PIT report was generated using PIT 1.16.1.

| Metric | Result |
|---|---:|
| PIT mutation coverage | **82% (76/93)** |
| PIT test strength | **92% (76/83)** |
| Total generated mutations | 93 |
| Killed mutations | 76 |
| Surviving covered mutations | 7 |
| Mutations without test coverage | 10 |

Mutation coverage measures how many generated mutations were killed by tests.

Test strength measures the percentage of mutations killed among those reached by tests.

### Mutation Coverage by Key Class

| Class | Mutation Coverage |
|---|---:|
| `GrossPayCalculator` | 100% (15/15) |
| `PayrollCalculator` | 100% (6/6) |
| `Money` | 100% (4/4) |
| `PercentageTaxRule` | 87% (27/31) |
| `TaxCalculator` | 88% (22/25) |
| `TaxRuleConfig` | 100% (1/1) |

The report identifies seven surviving covered mutations in `PercentageTaxRule` and `TaxCalculator`, involving conditional boundaries.

These remaining mutations indicate opportunities to strengthen tests for tax-rule validation and boundary behaviour.

They should be examined individually before determining whether they represent genuine weaknesses or equivalent mutations.

## Additional Improvements

### Overlapping Tax Rule Detection

An additional safeguard was implemented to detect **overlapping tax rules**.

Instead of selecting the first matching tax rule, `TaxCalculator` checks whether multiple rules apply to the same gross pay.

If multiple rules match, an `IllegalStateException` is thrown.

This reduces the risk of incorrect tax deductions when Rosewood introduces new tax bands.

### Centralized Tax Configuration

Tax bands previously appeared in the demonstration class and several test files.

The introduction of `TaxRuleConfig` centralizes the default tax rules, reducing duplication and making changes easier to maintain.

### Reusable Test Fixtures

`PayrollTestFixtures` provides a common setup for payroll-related tests.

This reduces repeated dependency initialization and helps maintain consistency across unit, integration, and characterization tests.

### Explicit Monetary Rounding

The introduction of `Money` provides a shared monetary rounding policy.

This makes the rounding behavior visible, consistent, and easier to test.

## Documentation

The project includes the following supporting documents:

- [Analysis Check-In](Analysis.md) — Identifies code smells, SOLID violations, AI-generated test weaknesses, and their severity.
- [Refactoring Rationale](RATIONALE.md) — Explains architectural decisions, exception handling, tax extensibility, test improvements, and remaining limitations.

The project demonstrates how an initially AI-generated implementation can be evaluated, refactored, and validated through automated tests, code coverage analysis, and mutation testing.
