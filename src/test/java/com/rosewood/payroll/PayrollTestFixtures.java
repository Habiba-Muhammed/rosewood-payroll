package com.rosewood.payroll;

public final class PayrollTestFixtures {

    private PayrollTestFixtures() {
    }

    public static TaxCalculator createTaxCalculator() {
        return new TaxCalculator(TaxRuleConfig.defaultRules());
    }

    public static PayrollCalculator createPayrollCalculator() {
        return new PayrollCalculator(
                new InMemoryEmployeeRepository(),
                new GrossPayCalculator(),
                createTaxCalculator()
        );
    }
}