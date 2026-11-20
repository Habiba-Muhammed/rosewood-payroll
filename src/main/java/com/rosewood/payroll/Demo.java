package com.rosewood.payroll;

public class Demo {
    public static void main(String[] args) {
        EmployeeRepository employeeRepository =
                new InMemoryEmployeeRepository();

        GrossPayCalculator grossPayCalculator =
                new GrossPayCalculator();

        TaxCalculator taxCalculator = new TaxCalculator(
                TaxRuleConfig.defaultRules()
        );

        PayrollCalculator calculator = new PayrollCalculator(
                employeeRepository,
                grossPayCalculator,
                taxCalculator
        );

        System.out.println("Result e1, 40h @ 20 -> "
                + calculator.calculateNetPay("e1", 40, 20));

        System.out.println("Result e2, 50h @ 30 -> "
                + calculator.calculateNetPay("e2", 50, 30));

        System.out.println("Result e3, 40h @ 20 -> "
                + calculator.calculateNetPay("e3", 40, 20));
    }
}
