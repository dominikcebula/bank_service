package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.rest.ds.request.AccountOpenRequest;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.ValidatorException;

public class AccountOpenRequestValidator extends Validator<AccountOpenRequest> {

    private final Configuration configuration = Configuration.getInstance();

    @Override
    public void validate(AccountOpenRequest value) throws ValidatorException {
        assertConditionMet(value.getInitialDeposit() > 0, "Initial Deposit has to be greater than zero");
        assertConditionMet(value.getCurrency().equals(configuration.getCurrency()), String.format("Only %s accounts can be opened on this instance", configuration.getCurrency()));
    }
}
