package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.rest.ds.request.TransferMoneyRequest;
import com.dominikcebula.bank.service.rest.ds.response.TransferMoneyResponse;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.TransferMoneyRequestValidator;
import com.google.inject.Inject;

public class TransferMoneyRestAction extends AbstractValidatingRestAction<TransferMoneyRequest, TransferMoneyResponse> {

    public static final String TRANSFER_URI = "/transfer";

    private final BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Inject
    public TransferMoneyRestAction(GsonProvider gsonProvider, BankActionsFacadeInvoker bankActionsFacadeInvoker) {
        super(gsonProvider, TransferMoneyRequest.class, TransferMoneyResponse.class);
        this.bankActionsFacadeInvoker = bankActionsFacadeInvoker;
    }

    @Override
    Validator<TransferMoneyRequest> getRequestValidator() {
        return new TransferMoneyRequestValidator();
    }

    @Override
    TransferMoneyResponse handleRequest(TransferMoneyRequest request) throws Exception {
        bankActionsFacadeInvoker.transfer(request.getFrom(), request.getTo(), request.getAmount());

        return new TransferMoneyResponse(request);
    }
}
