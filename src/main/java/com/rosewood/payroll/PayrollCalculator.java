
package com.rosewood.payroll;

import java.math.BigDecimal;
import java.util.Objects;

public class PayrollCalculator {

    private final EmployeeRepository employeeRepository;
    private final GrossPayCalculator grossPayCalculator;
    private final TaxCalculator taxCalculator;

    public PayrollCalculator(
            EmployeeRepository employeeRepository,
            GrossPayCalculator grossPayCalculator,
            TaxCalculator taxCalculator) {

        this.employeeRepository = Objects.requireNonNull(
                employeeRepository,
                "Employee repository cannot be null"
        );

        this.grossPayCalculator = Objects.requireNonNull(
                grossPayCalculator,
                "Gross pay calculator cannot be null"
        );

        this.taxCalculator = Objects.requireNonNull(
                taxCalculator,
                "Tax calculator cannot be null"
        );
    }

    public BigDecimal calculateNetPay(
            String employeeId,
            BigDecimal hoursWorked,
            BigDecimal hourlyRate) {

        Employee employee = employeeRepository.findById(employeeId);

        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId);
        }

        BigDecimal grossPay =
                grossPayCalculator.calculate(hoursWorked, hourlyRate);

        BigDecimal taxAmount =
                taxCalculator.calculateTax(grossPay);

        return Money.round(grossPay.subtract(taxAmount));
    }

    public double calculateNetPay(
            String employeeId,
            double hoursWorked,
            double hourlyRate) {

        Employee employee = employeeRepository.findById(employeeId);

        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId);
        }

        double grossPay =
                grossPayCalculator.calculate(hoursWorked, hourlyRate);

        double taxAmount =
                taxCalculator.calculateTax(grossPay);

        if (!Double.isFinite(grossPay)
                || !Double.isFinite(taxAmount)) {
            throw new IllegalStateException(
                    "Non-finite payroll result"
            );
        }

        return Money.round(
                BigDecimal.valueOf(grossPay)
                        .subtract(BigDecimal.valueOf(taxAmount))
        ).doubleValue();
    }
}
