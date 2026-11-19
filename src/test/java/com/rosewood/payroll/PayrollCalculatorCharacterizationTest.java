package com.rosewood.payroll;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayrollCalculatorCharacterizationTest {
    @Test
    void shouldCalculateNetPayForRegularHours() {
        EmployeeRepository repository = new InMemoryEmployeeRepository();
        PayrollCalculator calculator = new PayrollCalculator(repository);

        double netPay = calculator.calculateNetPay("e1", 40, 20);

        assertEquals(760.0, netPay, 0.001);
    }

    @Test
    void shouldCalculateNetPayWithOvertime() {
        EmployeeRepository repository = new InMemoryEmployeeRepository();
        PayrollCalculator calculator = new PayrollCalculator(repository);

        double netPay = calculator.calculateNetPay("e2", 50, 30);

        assertEquals(1402.5, netPay, 0.001);
    }
}
