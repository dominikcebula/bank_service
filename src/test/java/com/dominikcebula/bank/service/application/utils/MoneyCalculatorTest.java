package com.dominikcebula.bank.service.application.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyCalculatorTest {

    private final MoneyCalculator moneyCalculator = new MoneyCalculator(new MoneyAmountRound());

    @ParameterizedTest
    @CsvSource({
            "1, 1, 2.00",
            "0.1, 0.1, 0.20",
            "0.01, 0.01, 0.02",
            "2.01, 2.01, 4.02",
            "2.5, 2.4, 4.90",
            "2.51, 2.43, 4.94",
            "2.519, 2.43, 4.94",
            "1000.519, 2000.43, 3000.94",
    })
    void shouldCalculateAdditionCorrectly(String a, String b, String expectedResult) {
        assertEquals(
                new BigDecimal(expectedResult),
                moneyCalculator.add(new BigDecimal(a), new BigDecimal(b))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 0.00",
            "1, 1.001, 0.00",
            "2.5, 2.4, 0.10",
            "2.05, 2.04, 0.01"
    })
    void shouldSubtractAdditionCorrectly(String a, String b, String expectedResult) {
        assertEquals(
                new BigDecimal(expectedResult),
                moneyCalculator.subtract(new BigDecimal(a), new BigDecimal(b))
        );
    }
}