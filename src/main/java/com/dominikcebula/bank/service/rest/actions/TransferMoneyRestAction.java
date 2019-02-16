package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ModelApiResponse;
import com.dominikcebula.bank.service.dto.TransferMoneyRequest;
import com.dominikcebula.bank.service.dto.TransferMoneyResponse;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.TransferMoneyRequestValidator;
import com.google.inject.Inject;

public class TransferMoneyRestAction extends AbstractValidatingRestAction<TransferMoneyRequest, TransferMoneyResponse> {

    public static final String TRANSFER_URI = "/transfer";

    private final BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Inject
    @SuppressWarnings("unused")
    public TransferMoneyRestAction(BankActionsFacadeInvoker bankActionsFacadeInvoker) {
        super(TransferMoneyRequest.class, TransferMoneyResponse.class);
        this.bankActionsFacadeInvoker = bankActionsFacadeInvoker;
    }

    @Override
    Validator<TransferMoneyRequest> getRequestValidator() {
        return new TransferMoneyRequestValidator();
    }

    @Override
    TransferMoneyResponse handleRequest(TransferMoneyRequest request) throws Exception {
        bankActionsFacadeInvoker.transfer(AccountId.createAccountNumber(request.getFrom()), AccountId.createAccountNumber(request.getTo()), request.getAmount());

        return new TransferMoneyResponse()
                .status(new ModelApiResponse().code(ApiCode.MONEY_TRANSFERRED))
                .from(request.getFrom())
                .to(request.getTo())
                .amount(request.getAmount());
    }
}
