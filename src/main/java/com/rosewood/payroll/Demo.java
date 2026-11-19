package com.rosewood.payroll;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        EmployeeRepository employeeRepository = new InMemoryEmployeeRepository();

        GrossPayCalculator grossPayCalculator = new GrossPayCalculator();

        TaxCalculator taxCalculator = new TaxCalculator(
                List.of(
                        new PercentageTaxRule(
                                Double.NEGATIVE_INFINITY, 1000, 0.05
                        ),
                        new PercentageTaxRule(
                                1000, 5000, 0.15
                        ),
                        new PercentageTaxRule(
                                5000, Double.POSITIVE_INFINITY, 0.25
                        )
                )
        );

        PayrollCalculator calculator = new PayrollCalculator(
                employeeRepository,
                grossPayCalculator,
                taxCalculator
        );

        System.out.println("Result e1, 40h @ 20     -> " + calculator.calculateNetPay("e1", 40, 20));
        System.out.println("Result e2, 50h @ 30     -> " + calculator.calculateNetPay("e2", 50, 30));
        try {
            double netPay = calculator.calculateNetPay("unknown", 40, 20);

            System.out.println("Result unknown employee -> " + netPay);

        } catch (EmployeeNotFoundException exception) {
            System.err.println("Payroll error: " + exception.getMessage());
        }
    }
}
