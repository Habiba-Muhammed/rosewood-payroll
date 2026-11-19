
package com.rosewood.payroll;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PayrollCalculatorTest {

    @Test
    void calculatesNetPay() {

        PayrollCalculator calculator = createCalculator();

        double netPay = calculator.calculateNetPay("e1", 40, 20);

        assertEquals(760.0, netPay, 0.001);
    }

    @Test
    void calculatesOvertimePay() {

        PayrollCalculator calculator = createCalculator();

        double netPay = calculator.calculateNetPay("e2", 50, 30);

        assertEquals(1402.5, netPay, 0.001);
    }

    @Test
    void returnsPayForAnEmployee() {

        PayrollCalculator calculator = createCalculator();


        double netPay = calculator.calculateNetPay("e1", 35, 25);


        assertEquals(831.25, netPay, 0.001);
    }

    private PayrollCalculator createCalculator() {
        EmployeeRepository employeeRepository =
                new InMemoryEmployeeRepository();

        GrossPayCalculator grossPayCalculator =
                new GrossPayCalculator();

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

        return new PayrollCalculator(
                employeeRepository,
                grossPayCalculator,
                taxCalculator
        );
    }
}
