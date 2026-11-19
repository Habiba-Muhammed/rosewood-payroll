
package com.rosewood.payroll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PayrollIntegrationTest {

    private PayrollCalculator calculator;

    @BeforeEach
    void setUp() {
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

        calculator = new PayrollCalculator(
                employeeRepository,
                grossPayCalculator,
                taxCalculator
        );
    }

    @Test
    void calculatesNetPayUsingLowTaxBand() {

        double netPay = calculator.calculateNetPay("e1", 35, 20);

        assertEquals(665.0, netPay, 0.001);
    }

    @Test
    void calculatesNetPayWithOvertimeAndMediumTaxBand() {

        double netPay = calculator.calculateNetPay("e2", 44, 25);

        assertEquals(977.5, netPay, 0.001);
    }

    @Test
    void calculatesNetPayUsingHighTaxBand() {

        double netPay = calculator.calculateNetPay("e1", 40, 125);

        assertEquals(3750.0, netPay, 0.001);
    }

    @Test
    void throwsExceptionForMissingEmployee() {
        assertThrows(
                EmployeeNotFoundException.class,
                () -> calculator.calculateNetPay("unknown", 40, 20)
        );
    }

    @Test
    void propagatesInvalidHoursException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateNetPay("e1", -10, 20)
        );
    }
}
