package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ModelApiResponse;
import com.dominikcebula.bank.service.dto.MoneyTransfer;
import com.dominikcebula.bank.service.dto.TransferMoneyResponse;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.MoneyTransferValidator;
import com.google.inject.Inject;

public class TransferMoneyRestAction extends AbstractValidatingRestAction<MoneyTransfer, TransferMoneyResponse> {

    public static final String TRANSFER_URI = "/transfer";

    private final BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Inject
    @SuppressWarnings("unused")
    public TransferMoneyRestAction(BankActionsFacadeInvoker bankActionsFacadeInvoker) {
        super(MoneyTransfer.class, TransferMoneyResponse.class);
        this.bankActionsFacadeInvoker = bankActionsFacadeInvoker;
    }

    @Override
    Validator<MoneyTransfer> getRequestValidator() {
        return new MoneyTransferValidator();
    }

    @Override
    TransferMoneyResponse handleRequest(MoneyTransfer request) throws Exception {
        bankActionsFacadeInvoker.transfer(AccountId.createAccountNumber(request.getFrom()), AccountId.createAccountNumber(request.getTo()), request.getAmount());

        return new TransferMoneyResponse()
                .moneyTransfer(request)
                .status(new ModelApiResponse().code(ApiCode.MONEY_TRANSFERRED));
    }
}
