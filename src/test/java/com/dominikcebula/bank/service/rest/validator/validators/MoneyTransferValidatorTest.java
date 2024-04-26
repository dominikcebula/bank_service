package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.dto.MoneyTransfer;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static com.dominikcebula.bank.service.rest.validator.validators.AccountIdValidator.MESSAGE_ACCOUNT_ID_INCORRECT;
import static com.dominikcebula.bank.service.rest.validator.validators.AmountValidator.*;
import static com.dominikcebula.bank.service.rest.validator.validators.MoneyTransferValidator.*;

@RunWith(JUnitParamsRunner.class)
public class MoneyTransferValidatorTest {

    private static final AccountId FROM = AccountId.createRandomAccountId();
    private static final AccountId TO = AccountId.createRandomAccountId();
    private static final BigDecimal AMOUNT_POSITIVE = BigDecimal.valueOf(5);

    private final MoneyTransferValidator moneyTransferValidator = new MoneyTransferValidator();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldProcessRequestCorrectly() throws ValidatorException {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(AMOUNT_POSITIVE);

        moneyTransferValidator.validate(moneyTransfer);
    }

    @Test
    public void shouldReportTransferObjectMissing() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_TRANSFER_REQUEST_NOT_SPECIFIED);

        moneyTransferValidator.validate(null);
    }

    @Test
    public void shouldReportAmountMissing() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_VALUE_MISSING);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo(TO.getAccountNumber());

        moneyTransferValidator.validate(moneyTransfer);
    }

    @Test
    @Parameters({"0", "-5"})
    public void shouldReportAmountValueIncorrect(BigDecimal amount) throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_VALUE_ZERO);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(amount);

        moneyTransferValidator.validate(moneyTransfer);
    }

    @Test
    @Parameters({"4.567", "1.32354"})
    public void shouldReportAmountPatternIncorrect(BigDecimal amount) throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_VALUE_NOT_MATCHING_PATTERN);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(amount);

        moneyTransferValidator.validate(moneyTransfer);
    }

    @Test
    public void shouldReportFromMissing() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_FROM_ACCOUNT_NOT_SPECIFIED);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(AMOUNT_POSITIVE);

        moneyTransferValidator.validate(moneyTransfer);
    }

    @Test
    public void shouldReportToMissing() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_TO_ACCOUNT_NOT_SPECIFIED);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setAmount(AMOUNT_POSITIVE);

        moneyTransferValidator.validate(moneyTransfer);
    }

    @Test
    public void shouldReportFromIncorrect() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_ACCOUNT_ID_INCORRECT);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom("123");
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(AMOUNT_POSITIVE);

        moneyTransferValidator.validate(moneyTransfer);
    }

    @Test
    public void shouldReportToIncorrect() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_ACCOUNT_ID_INCORRECT);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo("12345678901234567");
        moneyTransfer.setAmount(AMOUNT_POSITIVE);

        moneyTransferValidator.validate(moneyTransfer);
    }
}