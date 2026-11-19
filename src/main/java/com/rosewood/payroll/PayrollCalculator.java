
package com.rosewood.payroll;

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

    public double calculateNetPay(
            String employeeId,
            double hoursWorked,
            double hourlyRate) {

        try {
            Employee employee = employeeRepository.findById(employeeId);

            double grossPay = grossPayCalculator.calculate(
                    hoursWorked,
                    hourlyRate
            );

            double taxRate;

            double taxAmount = taxCalculator.calculateTax(grossPay);
            double netPay = grossPay - taxAmount;

            System.out.println(
                    "Net pay for " + employee.getName() + ": GHS " + netPay
            );

            return netPay;

        } catch (Exception exception) {
            return 0;
        }
    }
}
