
package com.rosewood.payroll;

public class PercentageTaxRule implements TaxRule {

    private final double minimumGrossPay;
    private final double maximumGrossPay;
    private final double taxRate;

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

        this.minimumGrossPay = minimumGrossPay;
        this.maximumGrossPay = maximumGrossPay;
        this.taxRate = taxRate;
    }

    @Override
    public boolean appliesTo(double grossPay) {
        return grossPay >= minimumGrossPay
                && grossPay < maximumGrossPay;
    }

    @Override
    public double calculateTax(double grossPay) {
        return grossPay * taxRate;
    }
}
