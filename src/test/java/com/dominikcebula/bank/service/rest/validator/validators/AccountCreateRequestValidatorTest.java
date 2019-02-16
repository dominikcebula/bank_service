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

import static com.dominikcebula.bank.service.rest.validator.validators.AccountCreateRequestValidator.*;

@RunWith(JUnitParamsRunner.class)
public class AccountCreateRequestValidatorTest {

    private AccountCreateRequestValidator accountCreateRequestValidator = new AccountCreateRequestValidator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldProcessRequestCorrectly() throws ValidatorException {
        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(getMoney(5));

        accountCreateRequestValidator.validate(accountCreateRequest);
    }

    @Test
    public void shouldReportMissingRequestObject() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_ACCOUNT_CREATE_NOT_SPECIFIED);

        accountCreateRequestValidator.validate(null);
    }

    @Test
    public void shouldReportMissingDeposit() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_DEPOSIT_NOT_SPECIFIED);

        accountCreateRequestValidator.validate(new AccountCreateRequest());
    }

    @Test
    @Parameters({"0", "-5"})
    public void shouldReportIncorrectDeposit(int deposit) throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_DEPOSIT_INCORRECT);

        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(getMoney(deposit));

        accountCreateRequestValidator.validate(accountCreateRequest);
    }

    private BigDecimal getMoney(int amount) {
        return BigDecimal.valueOf(amount);
    }
}