package com.dominikcebula.bank.service.bls.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyAmountRound {
    public BigDecimal round(BigDecimal value) {
        return value.setScale(2, RoundingMode.DOWN);
    }
}
