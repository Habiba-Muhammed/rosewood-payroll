package com.rosewood.payroll;

public interface TaxRule {
   boolean appliesTo(double grossPay);

    double calculateTax(double grossPay);
}
