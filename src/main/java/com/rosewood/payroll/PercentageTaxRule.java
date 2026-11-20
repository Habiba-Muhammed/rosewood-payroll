
package com.rosewood.payroll;

import java.math.BigDecimal;
import java.util.Objects;

public class PercentageTaxRule implements TaxRule {

    private final BigDecimal minimumGrossPay;
    private final BigDecimal maximumGrossPay;
    private final BigDecimal taxRate;

    public PercentageTaxRule(
            double minimumGrossPay,
            double maximumGrossPay,
            double taxRate) {

        if (Double.isNaN(minimumGrossPay)
                || Double.isNaN(maximumGrossPay)
                || minimumGrossPay >= maximumGrossPay) {
            throw new IllegalArgumentException(
                    "Invalid tax band boundaries"
            );
        }

        if (!Double.isFinite(taxRate)
                || taxRate < 0
                || taxRate > 1) {
            throw new IllegalArgumentException(
                    "Tax rate must be between 0 and 1"
            );
        }

        this.minimumGrossPay =
                minimumGrossPay == Double.NEGATIVE_INFINITY
                        ? null
                        : BigDecimal.valueOf(minimumGrossPay);

        this.maximumGrossPay =
                maximumGrossPay == Double.POSITIVE_INFINITY
                        ? null
                        : BigDecimal.valueOf(maximumGrossPay);

        this.taxRate = BigDecimal.valueOf(taxRate);
    }

    public PercentageTaxRule(
            BigDecimal minimumGrossPay,
            BigDecimal maximumGrossPay,
            BigDecimal taxRate) {

        if (minimumGrossPay != null
                && maximumGrossPay != null
                && minimumGrossPay.compareTo(maximumGrossPay) >= 0) {
            throw new IllegalArgumentException(
                    "Invalid tax band boundaries"
            );
        }

        Objects.requireNonNull(taxRate, "Tax rate cannot be null");

        if (taxRate.compareTo(BigDecimal.ZERO) < 0
                || taxRate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException(
                    "Tax rate must be between 0 and 1"
            );
        }

        this.minimumGrossPay = minimumGrossPay;
        this.maximumGrossPay = maximumGrossPay;
        this.taxRate = taxRate;
    }

    @Override
    public boolean appliesTo(double grossPay) {
        return Double.isFinite(grossPay)
                && appliesTo(BigDecimal.valueOf(grossPay));
    }

    @Override
    public boolean appliesTo(BigDecimal grossPay) {
        Objects.requireNonNull(grossPay, "Gross pay cannot be null");

        return (minimumGrossPay == null
                || grossPay.compareTo(minimumGrossPay) >= 0)
                && (maximumGrossPay == null
                || grossPay.compareTo(maximumGrossPay) < 0);
    }

    @Override
    public double calculateTax(double grossPay) {
        return calculateTax(
                Money.fromDouble(grossPay, "Gross pay")
        ).doubleValue();
    }

    @Override
    public BigDecimal calculateTax(BigDecimal grossPay) {
        Objects.requireNonNull(grossPay, "Gross pay cannot be null");

        return Money.round(grossPay.multiply(taxRate));
    }
}
