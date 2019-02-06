package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.rest.ds.request.TransferMoneyRequest;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;

public class TransferMoneyRequestValidator extends Validator<TransferMoneyRequest> {

    static final String MESSAGE_AMOUNT_NOT_SPECIFIED = "Amount to transfer has to be specified";
    public static final String MESSAGE_AMOUNT_VALUE_INCORRECT = "Amount to transfer has to be positive";
    static final String MESSAGE_FROM_ACCOUNT_NOT_SPECIFIED = "From account has to be specified";
    static final String MESSAGE_TO_ACCOUNT_NOT_SPECIFIED = "To account has to be specified";

    @Override
    public void validate(TransferMoneyRequest transferMoneyRequest) throws ValidatorException {

        assertConditionMet(
                transferMoneyRequest.getAmount() != null,
                MESSAGE_AMOUNT_NOT_SPECIFIED
        );

        assertConditionMet(
                transferMoneyRequest.getAmount().isPositive(),
                MESSAGE_AMOUNT_VALUE_INCORRECT
        );

        assertConditionMet(
                transferMoneyRequest.getFrom() != null,
                MESSAGE_FROM_ACCOUNT_NOT_SPECIFIED
        );

        assertConditionMet(
                transferMoneyRequest.getTo() != null,
                MESSAGE_TO_ACCOUNT_NOT_SPECIFIED
        );
    }
}
