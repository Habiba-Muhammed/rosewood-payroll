
package com.rosewood.payroll;

import java.math.BigDecimal;

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

        System.out.println(
                "Result e1, 40h @ 20     -> "
                        + calculator.calculateNetPay(
                        "e1",
                        new BigDecimal("40"),
                        new BigDecimal("20")
                )
        );

        System.out.println(
                "Result e2, 50h @ 30     -> "
                        + calculator.calculateNetPay(
                        "e2",
                        new BigDecimal("50"),
                        new BigDecimal("30")
                )
        );

        try {
            BigDecimal netPay = calculator.calculateNetPay(
                    "unknown",
                    new BigDecimal("40"),
                    new BigDecimal("20")
            );

            System.out.println(
                    "Result unknown employee -> " + netPay
            );

        } catch (EmployeeNotFoundException exception) {
            System.err.println(
                    "Payroll error: " + exception.getMessage()
            );
        }
    }
}
