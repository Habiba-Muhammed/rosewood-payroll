package com.rosewood.payroll;

public class GrossPayCalculator {

    private static final double REGULAR_HOURS_LIMIT = 40.0;
    private static final double OVERTIME_PREMIUM = 0.5;

    public double calculate(double hoursWorked, double hourlyRate) {
        double grossPay = hoursWorked * hourlyRate;

        if (hoursWorked > REGULAR_HOURS_LIMIT) {
            double overtimeHours = hoursWorked - REGULAR_HOURS_LIMIT;
            double overtimePremium = overtimeHours * hourlyRate * OVERTIME_PREMIUM;

            grossPay += overtimePremium;
        }

        return grossPay;
    }
}
