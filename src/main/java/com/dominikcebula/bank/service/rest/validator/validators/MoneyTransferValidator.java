package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.dto.MoneyTransfer;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;

public class MoneyTransferValidator extends Validator<MoneyTransfer> {

    static final String MESSAGE_TRANSFER_REQUEST_NOT_SPECIFIED = "Transfer request object not specified";
    static final String MESSAGE_FROM_ACCOUNT_NOT_SPECIFIED = "From account has to be specified";
    static final String MESSAGE_TO_ACCOUNT_NOT_SPECIFIED = "To account has to be specified";

    private final AccountIdValidator accountIdValidator = new AccountIdValidator();
    private final AmountValidator amountValidator = new AmountValidator();

    @Override
    public void validate(MoneyTransfer moneyTransfer) throws ValidatorException {

        assertConditionMet(
                moneyTransfer != null,
                MESSAGE_TRANSFER_REQUEST_NOT_SPECIFIED
        );

        assertConditionMet(
                moneyTransfer.getFrom() != null,
                MESSAGE_FROM_ACCOUNT_NOT_SPECIFIED
        );

        assertConditionMet(
                moneyTransfer.getTo() != null,
                MESSAGE_TO_ACCOUNT_NOT_SPECIFIED
        );

        accountIdValidator.validate(moneyTransfer.getFrom());
        accountIdValidator.validate(moneyTransfer.getTo());

        amountValidator.validate(moneyTransfer.getAmount());
    }
}
