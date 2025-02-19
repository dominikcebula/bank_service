package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.dominikcebula.bank.service.rest.validator.validators.AccountIdValidator.MESSAGE_ACCOUNT_ID_INCORRECT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountIdValidatorTest {

    private final AccountIdValidator accountIdValidator = new AccountIdValidator();

    @ParameterizedTest
    @ValueSource(strings = {
            "1234567890123456",
            "6538908384675403",
            "4803402993553314"
    })
    void shouldValidateAccount(String accountNumber) {
        accountIdValidator.validate(accountNumber);
    }

    @Test
    void shouldValidateRandomlyGenerateAccount() {
        accountIdValidator.validate(String.valueOf(AccountId.createRandomAccountId()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1",
            "123",
            "123456789012345a",
            "a234567890123456",
            "12345678901234567",
            "123456789012345;",
            "123456'890123456"
    })
    void shouldReportInvalidAccount(String accountNumber) {
        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> accountIdValidator.validate(accountNumber));

        assertEquals(MESSAGE_ACCOUNT_ID_INCORRECT, thrownException.getMessage());
    }

    @Test
    void shouldReportNullAsInvalidAccount() {
        ValidatorException thrownException = assertThrows(ValidatorException.class, () -> accountIdValidator.validate(null));

        assertEquals(MESSAGE_ACCOUNT_ID_INCORRECT, thrownException.getMessage());
    }
}
