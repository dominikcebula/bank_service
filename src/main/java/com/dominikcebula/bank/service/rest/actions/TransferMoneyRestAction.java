package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacade;
import com.dominikcebula.bank.service.rest.ds.request.TransferMoneyRequest;
import com.dominikcebula.bank.service.rest.ds.response.TransferMoneyResponse;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.TransferMoneyRequestValidator;
import com.google.inject.Inject;

public class TransferMoneyRestAction extends AbstractValidatingRestAction<TransferMoneyRequest, TransferMoneyResponse> {

    public static final String TRANSFER_URI = "/transfer";

    private final BankActionsFacade bankActionsFacade;

    @Inject
    public TransferMoneyRestAction(GsonProvider gsonProvider, BankActionsFacade bankActionsFacade) {
        super(gsonProvider, TransferMoneyRequest.class, TransferMoneyResponse.class);
        this.bankActionsFacade = bankActionsFacade;
    }

    @Override
    Validator<TransferMoneyRequest> getRequestValidator() {
        return new TransferMoneyRequestValidator();
    }

    @Override
    TransferMoneyResponse handleRequest(TransferMoneyRequest request) throws Exception {
        bankActionsFacade.transfer(request.getFrom(), request.getTo(), request.getAmount());

        return new TransferMoneyResponse(request);
    }
}
