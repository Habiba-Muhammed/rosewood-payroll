
package com.rosewood.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GrossPayCalculatorTest {

    @Test
    void calculatesGrossPayForRegularHours() {

        GrossPayCalculator calculator = new GrossPayCalculator();

        double grossPay = calculator.calculate(35, 20);

        assertEquals(700.0, grossPay, 0.001);
    }

    @Test
    void calculatesGrossPayAtOvertimeThreshold() {

        GrossPayCalculator calculator = new GrossPayCalculator();

        double grossPay = calculator.calculate(40, 20);

        assertEquals(800.0, grossPay, 0.001);
    }

    @Test
    void calculatesGrossPayWithOvertime() {

        GrossPayCalculator calculator = new GrossPayCalculator();

        double grossPay = calculator.calculate(50, 30);

        assertEquals(1650.0, grossPay, 0.001);
    }

    @Test
    void calculatesGrossPayJustAboveOvertimeThreshold() {

        GrossPayCalculator calculator = new GrossPayCalculator();

        double grossPay = calculator.calculate(41, 20);

        assertEquals(830.0, grossPay, 0.001);
    }

    @Test
    void returnsZeroGrossPayForZeroHours() {

        GrossPayCalculator calculator = new GrossPayCalculator();

        double grossPay = calculator.calculate(0, 20);

        assertEquals(0.0, grossPay, 0.001);
    }
}
