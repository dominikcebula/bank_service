package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.rest.validator.Validator;

import static com.dominikcebula.bank.service.application.ds.AccountId.ACCOUNT_ID_LENGTH;

public class AccountIdValidator extends Validator<String> {

    static final String MESSAGE_ACCOUNT_ID_INCORRECT = "Account ID is incorrect";

    @Override
    public void validate(String value) {
        assertConditionMet(
                value != null,
                MESSAGE_ACCOUNT_ID_INCORRECT
        );

        assertConditionMet(
                value.matches("^[0-9]{" + ACCOUNT_ID_LENGTH + "}$"),
                MESSAGE_ACCOUNT_ID_INCORRECT
        );
    }
}
