package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.application.actions.BankActionsFacade;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.AccountCreateRequest;
import com.dominikcebula.bank.service.dto.AccountCreateResponse;
import com.dominikcebula.bank.service.dto.ModelApiResponse;
import com.dominikcebula.bank.service.rest.actions.base.AbstractValidatingRestAction;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.AccountCreateRequestValidator;
import com.google.inject.Inject;

import static com.dominikcebula.bank.service.dto.ApiCode.ACCOUNT_CREATED;

public class CreateAccountRestAction extends AbstractValidatingRestAction<AccountCreateRequest, AccountCreateResponse> {

    public static final String ACCOUNTS_CREATE_URI = "/accounts";

    private final BankActionsFacade bankActionsFacade;

    @Inject
    @SuppressWarnings("unused")
    public CreateAccountRestAction(BankActionsFacade bankActionsFacade) {
        super(AccountCreateRequest.class, AccountCreateResponse.class);
        this.bankActionsFacade = bankActionsFacade;
    }

    @Override
    protected Validator<AccountCreateRequest> getRequestValidator() {
        return new AccountCreateRequestValidator();
    }

    @Override
    protected AccountCreateResponse handleRequest(AccountCreateRequest accountCreateRequest) throws InterruptedException {
        Account account = bankActionsFacade.createAccount(accountCreateRequest.getInitialDeposit());
        ModelApiResponse status = new ModelApiResponse().code(ACCOUNT_CREATED);

        return new AccountCreateResponse()
                .status(status)
                .account(account);
    }
}
