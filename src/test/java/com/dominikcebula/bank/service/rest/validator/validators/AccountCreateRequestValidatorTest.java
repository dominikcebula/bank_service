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
import static com.dominikcebula.bank.service.rest.validator.validators.AmountValidator.MESSAGE_VALUE_INCORRECT;
import static com.dominikcebula.bank.service.rest.validator.validators.AmountValidator.MESSAGE_VALUE_MISSING;

@RunWith(JUnitParamsRunner.class)
public class AccountCreateRequestValidatorTest {

    private final AccountCreateRequestValidator accountCreateRequestValidator = new AccountCreateRequestValidator();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldProcessRequestCorrectly() throws ValidatorException {
        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(BigDecimal.valueOf(5));

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
        expectedException.expectMessage(MESSAGE_VALUE_MISSING);

        accountCreateRequestValidator.validate(new AccountCreateRequest());
    }

    @Test
    @Parameters({"0", "-5", "1.456"})
    public void shouldReportIncorrectDeposit(BigDecimal deposit) throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_VALUE_INCORRECT);

        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(deposit);

        accountCreateRequestValidator.validate(accountCreateRequest);
    }
}