package com.dominikcebula.bank.service.bls.utils;

import com.google.inject.Inject;

import java.math.BigDecimal;

public class MoneyCalculator {

    private final MoneyAmountRound moneyAmountRound;

    @Inject
    public MoneyCalculator(MoneyAmountRound moneyAmountRound) {
        this.moneyAmountRound = moneyAmountRound;
    }

    public BigDecimal add(BigDecimal a, BigDecimal b) {
        return rounded(a).add(rounded(b));
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return rounded(a).subtract(rounded(b));
    }

    private BigDecimal rounded(BigDecimal value) {
        return moneyAmountRound.round(value);
    }
}
