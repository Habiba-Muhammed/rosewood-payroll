
package com.rosewood.payroll;

import java.util.List;
import java.util.Objects;

public class TaxCalculator {

    private final List<TaxRule> taxRules;

    public TaxCalculator(List<TaxRule> taxRules) {
        Objects.requireNonNull(taxRules, "Tax rules cannot be null");

        if (taxRules.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one tax rule is required"
            );
        }

        this.taxRules = List.copyOf(taxRules);
    }

    public double calculateTax(double grossPay) {
        for (TaxRule taxRule : taxRules) {
            if (taxRule.appliesTo(grossPay)) {
                return taxRule.calculateTax(grossPay);
            }
        }

        throw new IllegalArgumentException(
                "No tax rule applies to gross pay: " + grossPay
        );
    }
}
