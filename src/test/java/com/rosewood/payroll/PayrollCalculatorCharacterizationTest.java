
package com.rosewood.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayrollCalculatorCharacterizationTest {

    @Test
    void calculatesNetPayForRegularHours() {
        PayrollCalculator calculator = PayrollTestFixtures.createPayrollCalculator();

        double netPay = calculator.calculateNetPay("e1", 40, 20);

        assertEquals(760.0, netPay, 0.001);
    }
}
