
package com.rosewood.payroll;

import java.util.List;

public final class TaxRuleConfig {

    private TaxRuleConfig() {
    }

    public static List<TaxRule> defaultRules() {
        return List.of(
                new PercentageTaxRule(0, 1000, 0.05),
                new PercentageTaxRule(1000, 5000, 0.15),
                new PercentageTaxRule(
                        5000,
                        Double.POSITIVE_INFINITY,
                        0.25
                )
        );
    }
}
