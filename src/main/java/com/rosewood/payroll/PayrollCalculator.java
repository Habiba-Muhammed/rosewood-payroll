
package com.rosewood.payroll;

import java.util.Objects;

public class PayrollCalculator {

    private final EmployeeRepository employeeRepository;
    private final GrossPayCalculator grossPayCalculator;

    public PayrollCalculator(
            EmployeeRepository employeeRepository,
            GrossPayCalculator grossPayCalculator) {

        this.employeeRepository = Objects.requireNonNull(
                employeeRepository,
                "Employee repository cannot be null"
        );

        this.grossPayCalculator = Objects.requireNonNull(
                grossPayCalculator,
                "Gross pay calculator cannot be null"
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

            if (grossPay < 1000) {
                taxRate = 0.05;
            } else if (grossPay < 5000) {
                taxRate = 0.15;
            } else {
                taxRate = 0.25;
            }

            double netPay = grossPay - (grossPay * taxRate);

            System.out.println(
                    "Net pay for " + employee.getName() + ": GHS " + netPay
            );

            return netPay;

        } catch (Exception exception) {
            return 0;
        }
    }
}
