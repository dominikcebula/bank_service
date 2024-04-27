package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.dto.AccountCreateRequest;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static com.dominikcebula.bank.service.rest.validator.validators.AccountCreateRequestValidator.MESSAGE_ACCOUNT_CREATE_NOT_SPECIFIED;
import static com.dominikcebula.bank.service.rest.validator.validators.AmountValidator.*;

@RunWith(JUnitParamsRunner.class)
public class AccountCreateRequestValidatorTest {

    private final AccountCreateRequestValidator accountCreateRequestValidator = new AccountCreateRequestValidator();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldProcessRequestCorrectly() {
        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(BigDecimal.valueOf(5));

        accountCreateRequestValidator.validate(accountCreateRequest);
    }

    @Test
    public void shouldReportMissingRequestObject() {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_ACCOUNT_CREATE_NOT_SPECIFIED);

        accountCreateRequestValidator.validate(null);
    }

    @Test
    public void shouldReportMissingDeposit() {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_VALUE_MISSING);

        accountCreateRequestValidator.validate(new AccountCreateRequest());
    }

    @Test
    @Parameters({"0", "-5"})
    public void shouldReportIncorrectDepositValue(BigDecimal deposit) {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_VALUE_ZERO);

        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(deposit);

        accountCreateRequestValidator.validate(accountCreateRequest);
    }

    @Test
    @Parameters({"5.84865", "1.456", "10000.0001", "8.123"})
    public void shouldReportIncorrectDepositPattern(BigDecimal deposit) {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_VALUE_NOT_MATCHING_PATTERN);

        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(deposit);

        accountCreateRequestValidator.validate(accountCreateRequest);
    }
}