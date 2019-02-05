package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.rest.ds.request.AccountOpenRequest;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;

public class AccountOpenRequestValidator extends Validator<AccountOpenRequest> {

    @Override
    public void validate(AccountOpenRequest accountOpenRequest) throws ValidatorException {
        assertConditionMet(
                accountOpenRequest.getInitialDeposit().isPositive(),
                "Initial Deposit has to be greater than zero"
        );
    }
}
