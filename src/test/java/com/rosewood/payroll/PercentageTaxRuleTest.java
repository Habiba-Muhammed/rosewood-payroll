
package com.rosewood.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PercentageTaxRuleTest {

    @Test
    void appliesToGrossPayWithinBand() {

        TaxRule taxRule = new PercentageTaxRule(
                1000, 5000, 0.15
        );

        assertTrue(taxRule.appliesTo(1500));
        assertTrue(taxRule.appliesTo(4000));
    }

    @Test
    void includesMinimumButExcludesMaximumBoundary() {

        TaxRule taxRule = new PercentageTaxRule(
                1000, 5000, 0.15
        );


        assertFalse(taxRule.appliesTo(999.99));
        assertTrue(taxRule.appliesTo(1000));
        assertTrue(taxRule.appliesTo(4999.99));
        assertFalse(taxRule.appliesTo(5000));
    }

    @Test
    void calculatesTaxUsingConfiguredRate() {

        TaxRule taxRule = new PercentageTaxRule(
                1000, 5000, 0.15
        );

        double taxAmount = taxRule.calculateTax(1650);

        assertEquals(247.50, taxAmount, 0.001);
    }


    @Test
    void rejectsReversedTaxBandBoundaries() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PercentageTaxRule(5000, 1000, 0.15)
        );
    }

    @Test
    void rejectsEqualTaxBandBoundaries() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PercentageTaxRule(1000, 1000, 0.15)
        );
    }

    @Test
    void rejectsNegativeTaxRate() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PercentageTaxRule(1000, 5000, -0.05)
        );
    }

    @Test
    void rejectsTaxRateGreaterThanOne() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PercentageTaxRule(1000, 5000, 1.5)
        );
    }

    @Test
    void rejectsNaNTaxRate() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PercentageTaxRule(1000, 5000, Double.NaN)
        );
    }

    @Test
    void rejectsNaNMinimumGrossPay() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PercentageTaxRule(
                        Double.NaN, 1000, 0.05
                )
        );
    }

    @Test
    void rejectsNaNMaximumGrossPay() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PercentageTaxRule(
                        0, Double.NaN, 0.05
                )
        );
    }


}
