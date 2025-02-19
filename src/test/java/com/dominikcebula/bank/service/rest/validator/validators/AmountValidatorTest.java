package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static com.dominikcebula.bank.service.rest.validator.validators.AmountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AmountValidatorTest {

    private final AmountValidator amountValidator = new AmountValidator();

    @ParameterizedTest
    @ValueSource(strings = {
            "1",
            "5",
            "10",
            "10000",
            "0.1",
            "0.01",
            "0.10",
            "0.50",
            "0.99",
            "100.56",
            "9.99",
            "9.00"
    })
    void shouldReportCorrectAmount(String amount) {
        amountValidator.validate(new BigDecimal(amount));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-1",
            "-5",
            "0"
    })
    void shouldReportIncorrectAmountValue(String amount) {
        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> amountValidator.validate(new BigDecimal(amount)));

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_VALUE_ZERO);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0.123",
            "5.999",
            "10500.5914"
    })
    void shouldReportIncorrectAmountPattern(String amount) {
        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> amountValidator.validate(new BigDecimal(amount)));

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_VALUE_NOT_MATCHING_PATTERN);
    }

    @Test
    void shouldReportMissingAmount() {
        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> amountValidator.validate(null));

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_VALUE_MISSING);
    }
}