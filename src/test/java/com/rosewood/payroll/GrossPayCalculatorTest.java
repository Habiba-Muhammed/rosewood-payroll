
package com.rosewood.payroll;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void rejectsNegativeHours() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(-10, 20)
        );
    }

    @Test
    void rejectsNegativeHourlyRate() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(40, -20)
        );
    }

    @Test
    void rejectsNaNHours() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(Double.NaN, 20)
        );
    }

    @Test
    void rejectsInfiniteHourlyRate() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(40, Double.POSITIVE_INFINITY)
        );
    }

    @Test
    void rejectsGrossPayOverflow() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        assertThrows(
                ArithmeticException.class,
                () -> calculator.calculate(Double.MAX_VALUE, 20)
        );
    }

    @Test
    void acceptsZeroHourlyRate() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        double grossPay = calculator.calculate(40, 0);

        assertEquals(0.0, grossPay, 0.001);
    }

    @Test
    void rejectsNullDecimalHours() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(null, new BigDecimal("20"))
        );
    }

    @Test
    void rejectsNegativeDecimalHours() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(
                        new BigDecimal("-1"),
                        new BigDecimal("20")
                )
        );
    }

    @Test
    void rejectsNullDecimalHourlyRate() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(new BigDecimal("40"), null)
        );
    }

    @Test
    void rejectsNegativeDecimalHourlyRate() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(
                        new BigDecimal("40"),
                        new BigDecimal("-0.01")
                )
        );
    }

    @Test
    void roundsDecimalGrossPayToTwoPlaces() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        BigDecimal grossPay = calculator.calculate(
                new BigDecimal("1"),
                new BigDecimal("0.005")
        );

        assertEquals(new BigDecimal("0.01"), grossPay);
    }
}
