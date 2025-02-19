package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.dto.MoneyTransfer;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static com.dominikcebula.bank.service.rest.validator.validators.AccountIdValidator.MESSAGE_ACCOUNT_ID_INCORRECT;
import static com.dominikcebula.bank.service.rest.validator.validators.AmountValidator.*;
import static com.dominikcebula.bank.service.rest.validator.validators.MoneyTransferValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTransferValidatorTest {

    private static final AccountId FROM = AccountId.createRandomAccountId();
    private static final AccountId TO = AccountId.createRandomAccountId();
    private static final BigDecimal AMOUNT_POSITIVE = BigDecimal.valueOf(5);

    private final MoneyTransferValidator moneyTransferValidator = new MoneyTransferValidator();

    @Test
    void shouldProcessRequestCorrectly() {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(AMOUNT_POSITIVE);

        moneyTransferValidator.validate(moneyTransfer);
    }

    @Test
    void shouldReportTransferObjectMissing() {
        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> moneyTransferValidator.validate(null));

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_TRANSFER_REQUEST_NOT_SPECIFIED);
    }

    @Test
    void shouldReportAmountMissing() {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo(TO.getAccountNumber());

        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> moneyTransferValidator.validate(moneyTransfer));

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_VALUE_MISSING);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-5"})
    void shouldReportAmountValueIncorrect(String amount) {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(new BigDecimal(amount));

        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> moneyTransferValidator.validate(moneyTransfer));

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_VALUE_ZERO);
    }

    @ParameterizedTest
    @ValueSource(strings = {"4.567", "1.32354"})
    void shouldReportAmountPatternIncorrect(String amount) {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(new BigDecimal(amount));

        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> moneyTransferValidator.validate(moneyTransfer));

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_VALUE_NOT_MATCHING_PATTERN);
    }

    @Test
    void shouldReportFromMissing() {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(AMOUNT_POSITIVE);

        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> moneyTransferValidator.validate(moneyTransfer));

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_FROM_ACCOUNT_NOT_SPECIFIED);
    }

    @Test
    void shouldReportToMissing() {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setAmount(AMOUNT_POSITIVE);

        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> moneyTransferValidator.validate(moneyTransfer));

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_TO_ACCOUNT_NOT_SPECIFIED);
    }

    @Test
    void shouldReportFromIncorrect() {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom("123");
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(AMOUNT_POSITIVE);

        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> moneyTransferValidator.validate(moneyTransfer));

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_ACCOUNT_ID_INCORRECT);
    }

    @Test
    void shouldReportToIncorrect() {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo("12345678901234567");
        moneyTransfer.setAmount(AMOUNT_POSITIVE);

        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> moneyTransferValidator.validate(moneyTransfer));

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_ACCOUNT_ID_INCORRECT);
    }
}