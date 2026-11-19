package com.rosewood.payroll;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayrollCalculatorCharacterizationTest {
    @Test
    void calculatesNetPayForRegularHours() {
        EmployeeRepository employeeRepository = new InMemoryEmployeeRepository();
        GrossPayCalculator grossPayCalculator = new GrossPayCalculator();

        PayrollCalculator calculator = new PayrollCalculator(
                employeeRepository,
                grossPayCalculator
        );

        double netPay = calculator.calculateNetPay("e1", 40, 20);

        assertEquals(760.0, netPay, 0.001);
    }
}