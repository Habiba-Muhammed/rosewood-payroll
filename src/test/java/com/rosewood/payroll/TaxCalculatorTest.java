
package com.rosewood.payroll;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    void rejectsEmptyTaxRules() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TaxCalculator(List.of())
        );
    }

    @Test
    void throwsExceptionWhenNoTaxRuleMatches() {
        TaxCalculator calculator = new TaxCalculator(
                List.of(
                        new PercentageTaxRule(0, 1000, 0.05)
                )
        );

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> calculator.calculateTax(1500)
        );

        assertEquals(
                "No tax rule applies to gross pay: 1500.0",
                exception.getMessage()
        );
    }

    @Test
    void throwsExceptionWhenTaxRulesOverlap() {
        TaxCalculator calculator = new TaxCalculator(
                List.of(
                        new PercentageTaxRule(0, 2000, 0.05),
                        new PercentageTaxRule(1000, 5000, 0.15)
                )
        );

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> calculator.calculateTax(1500)
        );

        assertEquals(
                "Multiple tax rules apply to gross pay: 1500.0",
                exception.getMessage()
        );
    }

    @Test
    void rejectsNegativeGrossPay() {
        TaxCalculator calculator = createCalculator();

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateTax(-100)
        );
    }

    @Test
    void rejectsNaNGrossPay() {
        TaxCalculator calculator = createCalculator();

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateTax(Double.NaN)
        );
    }

    @Test
    void rejectsTaxAmountGreaterThanGrossPay() {

        TaxRule taxRule = mock(TaxRule.class);

        when(taxRule.appliesTo(800.0))
                .thenReturn(true);

        when(taxRule.calculateTax(800.0))
                .thenReturn(900.0);

        TaxCalculator calculator = new TaxCalculator(
                List.of(taxRule)
        );

        assertThrows(
                IllegalStateException.class,
                () -> calculator.calculateTax(800.0)
        );
    }

    @Test
    void rejectsNegativeCalculatedTax() {
        TaxRule taxRule = mock(TaxRule.class);

        when(taxRule.appliesTo(800.0)).thenReturn(true);
        when(taxRule.calculateTax(800.0)).thenReturn(-10.0);

        TaxCalculator calculator = new TaxCalculator(
                List.of(taxRule)
        );

        assertThrows(
                IllegalStateException.class,
                () -> calculator.calculateTax(800.0)
        );
    }

    @Test
    void rejectsNaNCalculatedTax() {
        TaxRule taxRule = mock(TaxRule.class);

        when(taxRule.appliesTo(800.0)).thenReturn(true);
        when(taxRule.calculateTax(800.0)).thenReturn(Double.NaN);

        TaxCalculator calculator = new TaxCalculator(
                List.of(taxRule)
        );

        assertThrows(
                IllegalStateException.class,
                () -> calculator.calculateTax(800.0)
        );
    }

    @Test
    void acceptsZeroGrossPay() {
        TaxCalculator calculator = createCalculator();

        double taxAmount = calculator.calculateTax(0);

        assertEquals(0.0, taxAmount, 0.001);
    }

    @Test
    void acceptsZeroPercentTaxRate() {
        TaxCalculator calculator = new TaxCalculator(
                List.of(
                        new PercentageTaxRule(
                                0, Double.POSITIVE_INFINITY, 0.0
                        )
                )
        );

        double taxAmount = calculator.calculateTax(800);

        assertEquals(0.0, taxAmount, 0.001);
    }

    @Test
    void acceptsHundredPercentTaxRate() {
        TaxCalculator calculator = new TaxCalculator(
                List.of(
                        new PercentageTaxRule(
                                0, Double.POSITIVE_INFINITY, 1.0
                        )
                )
        );

        double taxAmount = calculator.calculateTax(800);

        assertEquals(800.0, taxAmount, 0.001);
    }

}
