
package com.rosewood.payroll;

import java.math.BigDecimal;

public class GrossPayCalculator {

    private static final BigDecimal REGULAR_HOURS_LIMIT =
            new BigDecimal("40");

    private static final BigDecimal OVERTIME_MULTIPLIER =
            new BigDecimal("1.5");

    public BigDecimal calculate(
            BigDecimal hoursWorked,
            BigDecimal hourlyRate) {

        if (hoursWorked == null || hoursWorked.signum() < 0) {
            throw new IllegalArgumentException(
                    "Hours worked must be a non-negative number"
            );
        }

        if (hourlyRate == null || hourlyRate.signum() < 0) {
            throw new IllegalArgumentException(
                    "Hourly rate must be a non-negative number"
            );
        }

        // At exactly 40 hours, overtimeHours is zero.
        BigDecimal regularHours =
                hoursWorked.min(REGULAR_HOURS_LIMIT);

        BigDecimal overtimeHours =
                hoursWorked.subtract(regularHours);

        BigDecimal regularPay =
                regularHours.multiply(hourlyRate);

        BigDecimal overtimePay = overtimeHours
                .multiply(hourlyRate)
                .multiply(OVERTIME_MULTIPLIER);

        return Money.round(regularPay.add(overtimePay));
    }

    public double calculate(double hoursWorked, double hourlyRate) {

        if (!Double.isFinite(hoursWorked) || hoursWorked < 0) {
            throw new IllegalArgumentException(
                    "Hours worked must be a finite, non-negative number"
            );
        }

        if (!Double.isFinite(hourlyRate) || hourlyRate < 0) {
            throw new IllegalArgumentException(
                    "Hourly rate must be a finite, non-negative number"
            );
        }

        double result = calculate(
                BigDecimal.valueOf(hoursWorked),
                BigDecimal.valueOf(hourlyRate)
        ).doubleValue();

        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Calculated gross pay exceeds the supported numeric range"
            );
        }

        return result;
    }
}
