
package com.rosewood.payroll;

public class GrossPayCalculator {

    private static final double REGULAR_HOURS_LIMIT = 40.0;
    private static final double OVERTIME_PREMIUM = 0.5;

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

        double grossPay = hoursWorked * hourlyRate;

        if (hoursWorked > REGULAR_HOURS_LIMIT) {
            double overtimeHours = hoursWorked - REGULAR_HOURS_LIMIT;
            double overtimePremium =
                    overtimeHours * hourlyRate * OVERTIME_PREMIUM;

            grossPay += overtimePremium;
        }

        if (!Double.isFinite(grossPay)) {
            throw new ArithmeticException(
                    "Calculated gross pay exceeds the supported numeric range"
            );
        }

        return grossPay;
    }
}
