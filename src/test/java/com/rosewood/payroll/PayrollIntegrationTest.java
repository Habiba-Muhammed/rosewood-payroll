
package com.rosewood.payroll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PayrollIntegrationTest {

    private PayrollCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = PayrollTestFixtures.createPayrollCalculator();
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
