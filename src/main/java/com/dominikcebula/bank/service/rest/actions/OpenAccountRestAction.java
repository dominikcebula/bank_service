package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacade;
import com.dominikcebula.bank.service.rest.ds.request.AccountOpenRequest;
import com.dominikcebula.bank.service.rest.ds.response.AccountOpenResponse;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.AccountOpenRequestValidator;
import com.google.inject.Inject;

public class OpenAccountRestAction extends AbstractValidatingRestAction<AccountOpenRequest, AccountOpenResponse> {

    private final BankActionsFacade bankActionsFacade;

    @Inject
    public OpenAccountRestAction(GsonProvider gsonProvider, BankActionsFacade bankActionsFacade) {
        super(gsonProvider, AccountOpenRequest.class, AccountOpenResponse.class);
        this.bankActionsFacade = bankActionsFacade;
    }

    @Override
    public Validator<AccountOpenRequest> getRequestValidator() {
        return new AccountOpenRequestValidator();
    }

    @Override
    public AccountOpenResponse handleRequest(AccountOpenRequest accountOpenRequest) throws Exception {
        return new AccountOpenResponse(
                bankActionsFacade.openAccount(
                        accountOpenRequest.getInitialDeposit()
                )
        );
    }
}
