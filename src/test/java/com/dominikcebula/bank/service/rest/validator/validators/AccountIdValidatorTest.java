package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static com.dominikcebula.bank.service.rest.validator.validators.AccountIdValidator.MESSAGE_ACCOUNT_ID_INCORRECT;

@RunWith(JUnitParamsRunner.class)
public class AccountIdValidatorTest {

    private final AccountIdValidator accountIdValidator = new AccountIdValidator();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    @Parameters({
            "1234567890123456",
            "6538908384675403",
            "4803402993553314"
    })
    public void shouldValidateAccount(String accountNumber) throws ValidatorException {
        accountIdValidator.validate(accountNumber);
    }

    @Test
    public void shouldValidateRandomlyGenerateAccount() throws ValidatorException {
        accountIdValidator.validate(String.valueOf(AccountId.createRandomAccountId()));
    }

    @Test
    @Parameters({
            "1",
            "123",
            "123456789012345a",
            "a234567890123456",
            "12345678901234567",
            "123456789012345;",
            "123456'890123456"
    })
    public void shouldReportInvalidAccount(String accountNumber) throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_ACCOUNT_ID_INCORRECT);

        accountIdValidator.validate(accountNumber);
    }

    @Test
    public void shouldReportNullAsInvalidAccount() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_ACCOUNT_ID_INCORRECT);

        accountIdValidator.validate(null);
    }
}