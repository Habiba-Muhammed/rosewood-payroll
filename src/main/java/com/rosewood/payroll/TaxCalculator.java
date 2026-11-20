
package com.rosewood.payroll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class TaxCalculator {

    private final List<TaxRule> taxRules;

    public TaxCalculator(List<TaxRule> taxRules) {
        Objects.requireNonNull(
                taxRules, "Tax rules cannot be null"
        );

        if (taxRules.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one tax rule is required"
            );
        }

        this.taxRules = List.copyOf(taxRules);
    }

    public BigDecimal calculateTax(BigDecimal grossPay) {

        if (grossPay == null || grossPay.signum() < 0) {
            throw new IllegalArgumentException(
                    "Gross pay must be a non-negative number"
            );
        }

        TaxRule matchingRule = null;

        for (TaxRule rule : taxRules) {
            if (rule.appliesTo(grossPay)) {
                if (matchingRule != null) {
                    throw new IllegalStateException(
                            "Multiple tax rules apply to gross pay: "
                                    + grossPay.toPlainString()
                    );
                }
                matchingRule = rule;
            }
        }

        if (matchingRule == null) {
            throw new IllegalStateException(
                    "No tax rule applies to gross pay: "
                            + grossPay.toPlainString()
            );
        }

        BigDecimal taxAmount = matchingRule.calculateTax(grossPay);

        if (taxAmount == null
                || taxAmount.signum() < 0
                || taxAmount.compareTo(grossPay) > 0) {
            throw new IllegalStateException(
                    "Invalid tax amount calculated: " + taxAmount
            );
        }

        return Money.round(taxAmount);
    }

    public double calculateTax(double grossPay) {

        if (!Double.isFinite(grossPay) || grossPay < 0) {
            throw new IllegalArgumentException(
                    "Gross pay must be a finite, non-negative number"
            );
        }

        TaxRule matchingRule = null;

        for (TaxRule rule : taxRules) {
            if (rule.appliesTo(grossPay)) {
                if (matchingRule != null) {
                    throw new IllegalStateException(
                            "Multiple tax rules apply to gross pay: "
                                    + grossPay
                    );
                }
                matchingRule = rule;
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

        return Money.round(
                BigDecimal.valueOf(taxAmount)
        ).doubleValue();
    }
}
