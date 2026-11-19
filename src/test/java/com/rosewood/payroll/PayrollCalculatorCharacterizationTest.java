package com.rosewood.payroll;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayrollCalculatorCharacterizationTest {
    @Test
    void calculatesNetPayForRegularHours() {
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

        double netPay = calculator.calculateNetPay("e1", 40, 20);

        assertEquals(760.0, netPay, 0.001);
    }
}