package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.rest.ds.request.AccountOpenRequest;
import com.dominikcebula.bank.service.rest.ds.response.AccountOpenResponse;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.AccountOpenRequestValidator;
import com.google.inject.Inject;

public class OpenAccountRestAction extends AbstractValidatingRestAction<AccountOpenRequest, AccountOpenResponse> {

    public static final String ACCOUNTS_OPEN_URI = "/accounts/open";

    private final BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Inject
    public OpenAccountRestAction(GsonProvider gsonProvider, BankActionsFacadeInvoker bankActionsFacadeInvoker) {
        super(gsonProvider, AccountOpenRequest.class, AccountOpenResponse.class);
        this.bankActionsFacadeInvoker = bankActionsFacadeInvoker;
    }

    @Override
    public Validator<AccountOpenRequest> getRequestValidator() {
        return new AccountOpenRequestValidator();
    }

    @Override
    public AccountOpenResponse handleRequest(AccountOpenRequest accountOpenRequest) throws Exception {
        return new AccountOpenResponse(
                bankActionsFacadeInvoker.openAccount(
                        accountOpenRequest.getInitialDeposit()
                )
        );
    }
}
