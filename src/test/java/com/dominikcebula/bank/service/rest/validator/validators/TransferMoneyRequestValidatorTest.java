package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.dto.TransferMoneyRequest;
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
import static com.dominikcebula.bank.service.rest.validator.validators.TransferMoneyRequestValidator.*;

@RunWith(JUnitParamsRunner.class)
public class TransferMoneyRequestValidatorTest {

    private static final AccountId FROM = AccountId.createRandomAccountId();
    private static final AccountId TO = AccountId.createRandomAccountId();
    private static final BigDecimal AMOUNT_POSITIVE = BigDecimal.valueOf(5);

    private final TransferMoneyRequestValidator transferMoneyRequestValidator = new TransferMoneyRequestValidator();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldProcessRequestCorrectly() throws ValidatorException {
        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM.getAccountNumber());
        transferMoneyRequest.setTo(TO.getAccountNumber());
        transferMoneyRequest.setAmount(AMOUNT_POSITIVE);

        transferMoneyRequestValidator.validate(transferMoneyRequest);
    }

    @Test
    public void shouldReportTransferObjectMissing() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_TRANSFER_REQUEST_NOT_SPECIFIED);

        transferMoneyRequestValidator.validate(null);
    }

    @Test
    public void shouldReportAmountMissing() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_VALUE_MISSING);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM.getAccountNumber());
        transferMoneyRequest.setTo(TO.getAccountNumber());

        transferMoneyRequestValidator.validate(transferMoneyRequest);
    }

    @Test
    @Parameters({"0", "-5"})
    public void shouldReportAmountValueIncorrect(BigDecimal amount) throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_VALUE_ZERO);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM.getAccountNumber());
        transferMoneyRequest.setTo(TO.getAccountNumber());
        transferMoneyRequest.setAmount(amount);

        transferMoneyRequestValidator.validate(transferMoneyRequest);
    }

    @Test
    @Parameters({"4.567", "1.32354"})
    public void shouldReportAmountPatternIncorrect(BigDecimal amount) throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_VALUE_NOT_MATCHING_PATTERN);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM.getAccountNumber());
        transferMoneyRequest.setTo(TO.getAccountNumber());
        transferMoneyRequest.setAmount(amount);

        transferMoneyRequestValidator.validate(transferMoneyRequest);
    }

    @Test
    public void shouldReportFromMissing() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_FROM_ACCOUNT_NOT_SPECIFIED);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setTo(TO.getAccountNumber());
        transferMoneyRequest.setAmount(AMOUNT_POSITIVE);

        transferMoneyRequestValidator.validate(transferMoneyRequest);
    }

    @Test
    public void shouldReportToMissing() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_TO_ACCOUNT_NOT_SPECIFIED);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM.getAccountNumber());
        transferMoneyRequest.setAmount(AMOUNT_POSITIVE);

        transferMoneyRequestValidator.validate(transferMoneyRequest);
    }

    @Test
    public void shouldReportFromIncorrect() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_ACCOUNT_ID_INCORRECT);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom("123");
        transferMoneyRequest.setTo(TO.getAccountNumber());
        transferMoneyRequest.setAmount(AMOUNT_POSITIVE);

        transferMoneyRequestValidator.validate(transferMoneyRequest);
    }

    @Test
    public void shouldReportToIncorrect() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_ACCOUNT_ID_INCORRECT);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM.getAccountNumber());
        transferMoneyRequest.setTo("12345678901234567");
        transferMoneyRequest.setAmount(AMOUNT_POSITIVE);

        transferMoneyRequestValidator.validate(transferMoneyRequest);
    }
}