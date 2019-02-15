package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.dto.AccountOpenRequest;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;

public class AccountOpenRequestValidator extends Validator<AccountOpenRequest> {

    static final String MESSAGE_DEPOSIT_NOT_SPECIFIED = "Initial Deposit has to be specified";
    public static final String MESSAGE_DEPOSIT_INCORRECT = "Initial Deposit has to be greater than zero";

    @Override
    public void validate(AccountOpenRequest accountOpenRequest) throws ValidatorException {
        assertConditionMet(
                accountOpenRequest.getInitialDeposit() != null,
                MESSAGE_DEPOSIT_NOT_SPECIFIED
        );

        assertConditionMet(
                accountOpenRequest.getInitialDeposit().floatValue() > 0,
                MESSAGE_DEPOSIT_INCORRECT
        );
    }
}
