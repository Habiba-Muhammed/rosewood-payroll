package com.rosewood.payroll;

public class PercentageTaxRule implements TaxRule{

    private final double minimumGrossPay;
    private final double maximumGrossPay;
    private final double taxRate;

    public PercentageTaxRule(
            double minimumGrossPay,
            double maximumGrossPay,
            double taxRate) {

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

