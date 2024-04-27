package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.application.actions.BankActionsFacade;
import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ModelApiResponse;
import com.dominikcebula.bank.service.dto.MoneyTransfer;
import com.dominikcebula.bank.service.dto.TransferMoneyResponse;
import com.dominikcebula.bank.service.rest.actions.base.AbstractValidatingRestAction;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.MoneyTransferValidator;
import com.google.inject.Inject;

public class TransferMoneyRestAction extends AbstractValidatingRestAction<MoneyTransfer, TransferMoneyResponse> {

    public static final String TRANSFER_URI = "/transfer";

    private final BankActionsFacade bankActionsFacade;

    @Inject
    @SuppressWarnings("unused")
    public TransferMoneyRestAction(BankActionsFacade bankActionsFacade) {
        super(MoneyTransfer.class, TransferMoneyResponse.class);
        this.bankActionsFacade = bankActionsFacade;
    }

    @Override
    protected Validator<MoneyTransfer> getRequestValidator() {
        return new MoneyTransferValidator();
    }

    @Override
    protected TransferMoneyResponse handleRequest(MoneyTransfer request) {
        bankActionsFacade.transfer(AccountId.createAccountNumber(request.getFrom()), AccountId.createAccountNumber(request.getTo()), request.getAmount());

        return new TransferMoneyResponse()
                .moneyTransfer(request)
                .status(new ModelApiResponse().code(ApiCode.MONEY_TRANSFERRED));
    }
}
