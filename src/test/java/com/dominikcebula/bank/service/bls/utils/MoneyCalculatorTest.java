package com.dominikcebula.bank.service.bls.utils;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

@RunWith(JUnitParamsRunner.class)
public class MoneyCalculatorTest {

    private final MoneyCalculator moneyCalculator = new MoneyCalculator(new MoneyAmountRound());

    @Test
    @Parameters({
            "1, 1, 2.00",
            "0.1, 0.1, 0.20",
            "0.01, 0.01, 0.02",
            "2.01, 2.01, 4.02",
            "2.5, 2.4, 4.90",
            "2.51, 2.43, 4.94",
            "2.519, 2.43, 4.94",
            "1000.519, 2000.43, 3000.94",
    })
    public void shouldCalculateAdditionCorrectly(BigDecimal a, BigDecimal b, BigDecimal expectedResult) {
        Assert.assertEquals(
                expectedResult,
                moneyCalculator.add(a, b)
        );
    }

    @Test
    @Parameters({
            "1, 1, 0.00",
            "1, 1.001, 0.00",
            "2.5, 2.4, 0.10",
            "2.05, 2.04, 0.01"
    })
    public void shouldSubtractAdditionCorrectly(BigDecimal a, BigDecimal b, BigDecimal expectedResult) {
        Assert.assertEquals(
                expectedResult,
                moneyCalculator.subtract(a, b)
        );
    }
}