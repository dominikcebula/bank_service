package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.dto.AccountCreateRequest;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static com.dominikcebula.bank.service.rest.validator.validators.AccountCreateRequestValidator.MESSAGE_ACCOUNT_CREATE_NOT_SPECIFIED;
import static com.dominikcebula.bank.service.rest.validator.validators.AmountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountCreateRequestValidatorTest {

    private final AccountCreateRequestValidator accountCreateRequestValidator = new AccountCreateRequestValidator();

    @Test
    void shouldProcessRequestCorrectly() {
        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(BigDecimal.valueOf(5));

        accountCreateRequestValidator.validate(accountCreateRequest);
    }

    @Test
    void shouldReportMissingRequestObject() {
        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> {
            accountCreateRequestValidator.validate(null);
        });

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_ACCOUNT_CREATE_NOT_SPECIFIED);
    }

    @Test
    void shouldReportMissingDeposit() {
        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> {
            accountCreateRequestValidator.validate(new AccountCreateRequest());
        });

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_VALUE_MISSING);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-5"})
    void shouldReportIncorrectDepositValue(String deposit) {
        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(new BigDecimal(deposit));

        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> {
            accountCreateRequestValidator.validate(accountCreateRequest);
        });

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_VALUE_ZERO);
    }

    @ParameterizedTest
    @ValueSource(strings = {"5.84865", "1.456", "10000.0001", "8.123"})
    void shouldReportIncorrectDepositPattern(String deposit) {
        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(new BigDecimal(deposit));

        ValidatorException validatorException = assertThrows(ValidatorException.class, () -> {
            accountCreateRequestValidator.validate(accountCreateRequest);
        });

        assertThat(validatorException.getMessage())
                .isEqualTo(MESSAGE_VALUE_NOT_MATCHING_PATTERN);
    }
}