
package com.rosewood.payroll;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyPrecisionTest {

    @Test
    void roundsHalfCentTaxUpToOneCent() {
        PercentageTaxRule rule = new PercentageTaxRule(
                new BigDecimal("0"),
                null,
                new BigDecimal("0.05")
        );

        assertEquals(
                new BigDecimal("0.01"),
                rule.calculateTax(new BigDecimal("0.10"))
        );
    }

    @Test
    void calculatesOvertimePreciselyForHalfHour() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        assertEquals(
                new BigDecimal("815.00"),
                calculator.calculate(
                        new BigDecimal("40.5"),
                        new BigDecimal("20")
                )
        );
    }

    @Test
    void exactlyFortyHoursHasNoOvertimePremium() {
        GrossPayCalculator calculator = new GrossPayCalculator();

        assertEquals(
                new BigDecimal("800.00"),
                calculator.calculate(
                        new BigDecimal("40"),
                        new BigDecimal("20")
                )
        );
    }

    @Test
    void calculatesNetPayUsingExactDecimalArithmetic() {
        PayrollCalculator calculator =
                PayrollTestFixtures.createPayrollCalculator();

        assertEquals(
                new BigDecimal("0.09"),
                calculator.calculateNetPay(
                        "e1",
                        new BigDecimal("1"),
                        new BigDecimal("0.10")
                )
        );
    }

    @Test
    void preservesExistingPayrollExample() {
        PayrollCalculator calculator =
                PayrollTestFixtures.createPayrollCalculator();

        assertEquals(
                new BigDecimal("760.00"),
                calculator.calculateNetPay(
                        "e1",
                        new BigDecimal("40"),
                        new BigDecimal("20")
                )
        );
    }

    @Test
    void rejectsMissingEmployeeWithoutAbortingDemo() {
        PayrollCalculator calculator =
                PayrollTestFixtures.createPayrollCalculator();

        assertThrows(
                EmployeeNotFoundException.class,
                () -> calculator.calculateNetPay(
                        "unknown",
                        new BigDecimal("40"),
                        new BigDecimal("20")
                )
        );
    }

    @Test
    void rejectsNullMoneyAmount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Money.round(null)
        );
    }

    @Test
    void rejectsNonFiniteMoneyAmount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Money.fromDouble(Double.NaN, "Amount")
        );
    }
}
