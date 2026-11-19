
package com.rosewood.payroll;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaxCalculatorTest {

    @Test
    void calculatesTaxForLowBand() {

        TaxCalculator calculator = createCalculator();

        double taxAmount = calculator.calculateTax(800);

        assertEquals(40.0, taxAmount, 0.001);
    }

    @Test
    void calculatesTaxJustBelowMediumBand() {

        TaxCalculator calculator = createCalculator();

        double taxAmount = calculator.calculateTax(999);

        assertEquals(49.95, taxAmount, 0.001);
    }

    @Test
    void calculatesTaxAtMediumBandBoundary() {

        TaxCalculator calculator = createCalculator();

        double taxAmount = calculator.calculateTax(1000);

        assertEquals(150.0, taxAmount, 0.001);
    }

    @Test
    void calculatesTaxForMediumBand() {

        TaxCalculator calculator = createCalculator();

        double taxAmount = calculator.calculateTax(1650);

        assertEquals(247.50, taxAmount, 0.001);
    }

    @Test
    void calculatesTaxJustBelowHighBand() {

        TaxCalculator calculator = createCalculator();

        double taxAmount = calculator.calculateTax(4999);

        assertEquals(749.85, taxAmount, 0.001);
    }

    @Test
    void calculatesTaxAtHighBandBoundary() {

        TaxCalculator calculator = createCalculator();

        double taxAmount = calculator.calculateTax(5000);

        assertEquals(1250.0, taxAmount, 0.001);
    }

    @Test
    void calculatesTaxAboveHighBand() {

        TaxCalculator calculator = createCalculator();

        double taxAmount = calculator.calculateTax(6000);

        assertEquals(1500.0, taxAmount, 0.001);
    }

    private TaxCalculator createCalculator() {
        return new TaxCalculator(
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
    }
}
