package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.rest.ds.request.TransferMoneyRequest;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;

public class TransferMoneyRequestValidator extends Validator<TransferMoneyRequest> {

    private static final String MESSAGE_AMOUNT_NOT_SPECIFIED = "Amount to transfer has to be specified";
    public static final String AMOUNT_VALUE_INCORRECT = "Amount to transfer has to be positive";
    private static final String FROM_ACCOUNT_NOT_SPECIFIED = "From account has to be specified";
    private static final String TO_ACCOUNT_NOT_SPECIFIED = "To account has to be specified";

    @Override
    public void validate(TransferMoneyRequest transferMoneyRequest) throws ValidatorException {

        assertConditionMet(
                transferMoneyRequest.getAmount() != null,
                MESSAGE_AMOUNT_NOT_SPECIFIED
        );

        assertConditionMet(
                transferMoneyRequest.getAmount().isPositive(),
                AMOUNT_VALUE_INCORRECT
        );

        assertConditionMet(
                transferMoneyRequest.getFrom() != null,
                FROM_ACCOUNT_NOT_SPECIFIED
        );

        assertConditionMet(
                transferMoneyRequest.getTo() != null,
                TO_ACCOUNT_NOT_SPECIFIED
        );
    }
}
