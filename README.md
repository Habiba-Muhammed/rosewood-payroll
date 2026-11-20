# Rosewood Payroll — Net Pay Service

## Overview

Rosewood Payroll is a Java-based service that calculates employee net pay based on hours worked, hourly rates, overtime, and applicable tax bands.

This project was developed as part of the **AmaliTech Code Quality and Testing Applied Lab**.

The initial implementation was generated using an AI assistant and contained several code smells, SOLID violations, inadequate exception handling, and ineffective tests.

The service has been refactored to improve maintainability, extensibility, reliability, and testability using Clean Code principles, SOLID, JUnit 5, and Mockito.

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
| `EmployeeNotFoundException` | Reports missing employee errors |
| `Demo` | Demonstrates payroll calculations |

### SOLID Principles Applied

**Single Responsibility Principle (SRP):** Gross pay calculations, tax calculations, and employee retrieval are handled by separate components.

**Open/Closed Principle (OCP):** The `TaxRule` abstraction allows new tax rules to be introduced without modifying the existing calculator logic.

**Dependency Inversion Principle (DIP):** Dependencies are supplied through constructor injection rather than instantiated directly inside `PayrollCalculator`.

## Exception Handling

The application validates payroll inputs and reports failures through meaningful exceptions.

- Missing employees throw `EmployeeNotFoundException`.
- Negative or non-finite hours and hourly rates are rejected.
- Invalid tax-band configurations are rejected.
- Missing or overlapping tax rules trigger errors.
- Invalid calculated tax amounts are rejected.

Exceptions are propagated rather than silently converted into zero pay.

## Running the Application

### Requirements

- JDK 21
- Apache Maven

### Run the Demo

```bash
mvn compile
mvn exec:java -Dexec.mainClass=com.rosewood.payroll.Demo
```

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

The project uses JUnit 5 and Mockito to verify payroll calculations and failure behaviour.

**Unit tests** verify individual components, including gross pay, overtime, tax rules, boundaries, and validation.

**Mockito tests** isolate `PayrollCalculator` from its dependencies and verify its behaviour when dependencies return results or throw exceptions.

**Integration tests** verify that the real repository, gross pay calculator, tax calculator, and tax rules work together correctly.

The original AI-generated tests were evaluated and corrected to verify actual results rather than weak assertions or configured mock responses.

### Coverage and Mutation Testing Results

| Metric | Result |
|---|---|
| JaCoCo overall line coverage | 85.2% |
| JaCoCo branch coverage | 100% |
| PIT mutation coverage | 89% (47/53) |
| PIT test strength | 98% (47/48) |

The remaining surviving covered mutation changes the overtime condition from `> 40` to `>= 40`. This does not affect the calculated result at exactly 40 hours because the overtime premium is zero.

The overall line coverage includes the demonstration class, which is not exercised by the automated tests.

## Additional Improvement

An additional safeguard was implemented to detect **overlapping tax rules**.

Instead of selecting the first matching tax rule, `TaxCalculator` checks whether multiple rules apply to the same gross pay.

If multiple rules match, an `IllegalStateException` is thrown.

This reduces the risk of incorrect tax deductions when Rosewood introduces new tax bands.

## Documentation

The project includes the following supporting documents:

- [Analysis Check-In](Analysis.md) — Identified code smells, SOLID violations, AI-generated test weaknesses, and their severity.
- [Refactoring Rationale](RATIONALE.md) — Explains architectural decisions, exception handling, tax extensibility, test improvements, and remaining limitations.


The existing tax rates preserve the starter project's calculation rules and should not be interpreted as verified statutory tax rates.