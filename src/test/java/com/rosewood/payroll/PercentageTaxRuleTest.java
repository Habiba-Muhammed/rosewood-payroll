
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
}
