package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.dto.AccountOpenRequest;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static com.dominikcebula.bank.service.rest.validator.validators.AccountOpenRequestValidator.MESSAGE_DEPOSIT_INCORRECT;
import static com.dominikcebula.bank.service.rest.validator.validators.AccountOpenRequestValidator.MESSAGE_DEPOSIT_NOT_SPECIFIED;

@RunWith(JUnitParamsRunner.class)
public class AccountOpenRequestValidatorTest {

    private AccountOpenRequestValidator accountOpenRequestValidator = new AccountOpenRequestValidator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldProcessRequestCorrectly() throws ValidatorException {
        AccountOpenRequest accountOpenRequest = new AccountOpenRequest();
        accountOpenRequest.setInitialDeposit(getMoney(5));

        accountOpenRequestValidator.validate(accountOpenRequest);
    }

    @Test
    public void shouldReportMissingDeposit() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_DEPOSIT_NOT_SPECIFIED);

        accountOpenRequestValidator.validate(new AccountOpenRequest());
    }

    @Test
    @Parameters({"0", "-5"})
    public void shouldReportIncorrectDeposit(int deposit) throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_DEPOSIT_INCORRECT);

        AccountOpenRequest accountOpenRequest = new AccountOpenRequest();
        accountOpenRequest.setInitialDeposit(getMoney(deposit));

        accountOpenRequestValidator.validate(accountOpenRequest);
    }

    private BigDecimal getMoney(int amount) {
        return BigDecimal.valueOf(amount);
    }
}