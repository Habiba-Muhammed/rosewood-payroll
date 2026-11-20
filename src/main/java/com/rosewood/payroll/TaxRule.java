
package com.rosewood.payroll;

import java.math.BigDecimal;

public interface TaxRule {

    boolean appliesTo(double grossPay);

    double calculateTax(double grossPay);

    default boolean appliesTo(BigDecimal grossPay) {
        return appliesTo(grossPay.doubleValue());
    }

    default BigDecimal calculateTax(BigDecimal grossPay) {
        double tax = calculateTax(grossPay.doubleValue());

        if (!Double.isFinite(tax)) {
            throw new IllegalStateException(
                    "Invalid tax amount calculated: " + tax
            );
        }

        return Money.round(BigDecimal.valueOf(tax));
    }
}
