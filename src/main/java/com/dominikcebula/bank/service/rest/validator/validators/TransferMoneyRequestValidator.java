package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.dto.TransferMoneyRequest;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;

public class TransferMoneyRequestValidator extends Validator<TransferMoneyRequest> {

    static final String MESSAGE_TRANSFER_REQUEST_NOT_SPECIFIED = "Transfer request object not specified";
    static final String MESSAGE_AMOUNT_NOT_SPECIFIED = "Amount to transfer has to be specified";
    public static final String MESSAGE_AMOUNT_VALUE_INCORRECT = "Amount to transfer has to be positive";
    static final String MESSAGE_FROM_ACCOUNT_NOT_SPECIFIED = "From account has to be specified";
    static final String MESSAGE_TO_ACCOUNT_NOT_SPECIFIED = "To account has to be specified";

    private AccountIdValidator accountIdValidator = new AccountIdValidator();
    private AmountValidator amountValidator = new AmountValidator();

    @Override
    public void validate(TransferMoneyRequest transferMoneyRequest) throws ValidatorException {

        assertConditionMet(
                transferMoneyRequest != null,
                MESSAGE_TRANSFER_REQUEST_NOT_SPECIFIED
        );

        assertConditionMet(
                transferMoneyRequest.getAmount() != null,
                MESSAGE_AMOUNT_NOT_SPECIFIED
        );

        assertConditionMet(
                transferMoneyRequest.getAmount().floatValue() > 0,
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

        accountIdValidator.validate(transferMoneyRequest.getFrom());
        accountIdValidator.validate(transferMoneyRequest.getTo());

        amountValidator.validate(transferMoneyRequest.getAmount());
    }
}
