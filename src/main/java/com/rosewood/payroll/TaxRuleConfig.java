
package com.rosewood.payroll;

import java.math.BigDecimal;
import java.util.List;

public final class TaxRuleConfig {

    private TaxRuleConfig() {
    }

    public static List<TaxRule> defaultRules() {
        return List.of(
                new PercentageTaxRule(
                        new BigDecimal("0"),
                        new BigDecimal("1000"),
                        new BigDecimal("0.05")
                ),
                new PercentageTaxRule(
                        new BigDecimal("1000"),
                        new BigDecimal("5000"),
                        new BigDecimal("0.15")
                ),
                new PercentageTaxRule(
                        new BigDecimal("5000"),
                        null,
                        new BigDecimal("0.25")
                )
        );
    }
}
