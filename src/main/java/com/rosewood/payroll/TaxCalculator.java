
package com.rosewood.payroll;

import java.util.List;
import java.util.Objects;

public class TaxCalculator {

    private final List<TaxRule> taxRules;

    public TaxCalculator(List<TaxRule> taxRules) {
        Objects.requireNonNull(
                taxRules,
                "Tax rules cannot be null"
        );

        if (taxRules.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one tax rule is required"
            );
        }

        this.taxRules = List.copyOf(taxRules);
    }

    public double calculateTax(double grossPay) {

        if (!Double.isFinite(grossPay) || grossPay < 0) {
            throw new IllegalArgumentException(
                    "Gross pay must be a finite, non-negative number"
            );
        }

        TaxRule matchingRule = null;

        for (TaxRule taxRule : taxRules) {
            if (taxRule.appliesTo(grossPay)) {

                if (matchingRule != null) {
                    throw new IllegalStateException(
                            "Multiple tax rules apply to gross pay: "
                                    + grossPay
                    );
                }

                matchingRule = taxRule;
            }
        }

        if (matchingRule == null) {
            throw new IllegalStateException(
                    "No tax rule applies to gross pay: " + grossPay
            );
        }

        double taxAmount = matchingRule.calculateTax(grossPay);

        if (!Double.isFinite(taxAmount)
                || taxAmount < 0
                || taxAmount > grossPay) {

            throw new IllegalStateException(
                    "Invalid tax amount calculated: " + taxAmount
            );
        }

        return taxAmount;
    }
}
