package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.dto.AccountCreateRequest;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;

public class AccountCreateRequestValidator extends Validator<AccountCreateRequest> {

    static final String MESSAGE_ACCOUNT_CREATE_NOT_SPECIFIED = "Account Create Object has to be specified";

    private final AmountValidator amountValidator = new AmountValidator();

    @Override
    public void validate(AccountCreateRequest accountCreateRequest) throws ValidatorException {
        assertConditionMet(
                accountCreateRequest != null,
                MESSAGE_ACCOUNT_CREATE_NOT_SPECIFIED
        );

        amountValidator.validate(accountCreateRequest.getInitialDeposit());
    }
}
