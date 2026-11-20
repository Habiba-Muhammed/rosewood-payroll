
package com.rosewood.payroll;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Money {
    private Money() {
    }

    public static BigDecimal round(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException(
                    "Monetary amount cannot be null"
            );
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal fromDouble(double amount, String label) {
        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException(
                    label + " must be a finite number"
            );
        }
        return BigDecimal.valueOf(amount);
    }
}
