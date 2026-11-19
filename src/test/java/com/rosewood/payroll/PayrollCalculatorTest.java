
package com.rosewood.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PayrollCalculatorTest {

    @Test
    void calculatesNetPay() {
        EmployeeRepository employeeRepository = new InMemoryEmployeeRepository();
        GrossPayCalculator grossPayCalculator = new GrossPayCalculator();

        PayrollCalculator calculator = new PayrollCalculator(
                employeeRepository,
                grossPayCalculator
        );

        double netPay = calculator.calculateNetPay("e1", 40, 20);

        assertTrue(netPay > 0);
    }

    @Test
    void calculatesOvertimePay() {
        PayrollCalculator calculator = mock(PayrollCalculator.class);

        when(calculator.calculateNetPay("e2", 50, 30))
                .thenReturn(1402.5);

        assertEquals(
                1402.5,
                calculator.calculateNetPay("e2", 50, 30)
        );
    }

    @Test
    void returnsPayForAnEmployee() {
        EmployeeRepository employeeRepository = new InMemoryEmployeeRepository();
        GrossPayCalculator grossPayCalculator = new GrossPayCalculator();

        PayrollCalculator calculator = new PayrollCalculator(
                employeeRepository,
                grossPayCalculator
        );

        double netPay = calculator.calculateNetPay("e1", 35, 25);

        assertNotNull(netPay);
    }
}
