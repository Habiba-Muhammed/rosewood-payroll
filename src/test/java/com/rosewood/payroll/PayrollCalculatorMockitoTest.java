
package com.rosewood.payroll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayrollCalculatorMockitoTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private GrossPayCalculator grossPayCalculator;

    @Mock
    private TaxCalculator taxCalculator;

    private PayrollCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new PayrollCalculator(
                employeeRepository,
                grossPayCalculator,
                taxCalculator
        );
    }

    @Test
    void calculatesNetPayUsingInjectedDependencies() {

        Employee employee = new Employee("e1", "Efua Sarpong");

        when(employeeRepository.findById("e1"))
                .thenReturn(employee);

        when(grossPayCalculator.calculate(40, 20))
                .thenReturn(800.0);

        when(taxCalculator.calculateTax(800.0))
                .thenReturn(40.0);

        double netPay = calculator.calculateNetPay("e1", 40, 20);

        assertEquals(760.0, netPay, 0.001);

        verify(employeeRepository).findById("e1");
        verify(grossPayCalculator).calculate(40, 20);
        verify(taxCalculator).calculateTax(800.0);
    }

    @Test
    void throwsExceptionWhenEmployeeIsNotFound() {

        when(employeeRepository.findById("unknown"))
                .thenReturn(null);


        EmployeeNotFoundException exception = assertThrows(
                EmployeeNotFoundException.class,
                () -> calculator.calculateNetPay("unknown", 40, 20)
        );

        assertEquals(
                "Employee not found: unknown",
                exception.getMessage()
        );

        verifyNoInteractions(grossPayCalculator, taxCalculator);
    }

    @Test
    void propagatesGrossPayCalculationFailure() {

        when(employeeRepository.findById("e1"))
                .thenReturn(new Employee("e1", "Efua Sarpong"));

        when(grossPayCalculator.calculate(-10, 20))
                .thenThrow(new IllegalArgumentException(
                        "Invalid hours worked"
                ));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateNetPay("e1", -10, 20)
        );

        assertEquals("Invalid hours worked", exception.getMessage());

        verifyNoInteractions(taxCalculator);
    }

    @Test
    void propagatesTaxCalculationFailure() {

        when(employeeRepository.findById("e1"))
                .thenReturn(new Employee("e1", "Efua Sarpong"));

        when(grossPayCalculator.calculate(40, 20))
                .thenReturn(800.0);

        when(taxCalculator.calculateTax(800.0))
                .thenThrow(new IllegalStateException(
                        "No applicable tax rule"
                ));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> calculator.calculateNetPay("e1", 40, 20)
        );

        assertEquals(
                "No applicable tax rule",
                exception.getMessage()
        );
    }
}
