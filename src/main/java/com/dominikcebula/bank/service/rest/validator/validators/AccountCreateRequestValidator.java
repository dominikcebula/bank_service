package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.dto.AccountCreateRequest;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;

public class AccountCreateRequestValidator extends Validator<AccountCreateRequest> {

    static final String MESSAGE_DEPOSIT_NOT_SPECIFIED = "Initial Deposit has to be specified";
    public static final String MESSAGE_DEPOSIT_INCORRECT = "Initial Deposit has to be greater than zero";

    @Override
    public void validate(AccountCreateRequest accountCreateRequest) throws ValidatorException {
        assertConditionMet(
                accountCreateRequest.getInitialDeposit() != null,
                MESSAGE_DEPOSIT_NOT_SPECIFIED
        );

        assertConditionMet(
                accountCreateRequest.getInitialDeposit().floatValue() > 0,
                MESSAGE_DEPOSIT_INCORRECT
        );
    }
}
