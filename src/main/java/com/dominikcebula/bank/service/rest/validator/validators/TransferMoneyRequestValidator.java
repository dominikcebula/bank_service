package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.rest.ds.request.TransferMoneyRequest;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;

public class TransferMoneyRequestValidator extends Validator<TransferMoneyRequest> {

    @Override
    public void validate(TransferMoneyRequest transferMoneyRequest) throws ValidatorException {

        assertConditionMet(
                transferMoneyRequest.getAmount() != null,
                "Amount to transfer has to be specified"
        );

        assertConditionMet(
                transferMoneyRequest.getAmount().isPositive(),
                "Amount to transfer has to be positive"
        );

        assertConditionMet(
                transferMoneyRequest.getFrom() != null,
                "From account has to be specified"
        );

        assertConditionMet(
                transferMoneyRequest.getTo() != null,
                "To account has to be specified"
        );
    }
}
