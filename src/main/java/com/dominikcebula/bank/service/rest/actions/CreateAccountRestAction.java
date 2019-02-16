package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.AccountCreateRequest;
import com.dominikcebula.bank.service.dto.AccountCreateResponse;
import com.dominikcebula.bank.service.dto.ModelApiResponse;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.AccountCreateRequestValidator;
import com.google.inject.Inject;

import static com.dominikcebula.bank.service.dto.ApiCode.ACCOUNT_CREATED;

public class CreateAccountRestAction extends AbstractValidatingRestAction<AccountCreateRequest, AccountCreateResponse> {

    public static final String ACCOUNTS_CREATE_URI = "/accounts/create";

    private final BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Inject
    public CreateAccountRestAction(BankActionsFacadeInvoker bankActionsFacadeInvoker) {
        super(AccountCreateRequest.class, AccountCreateResponse.class);
        this.bankActionsFacadeInvoker = bankActionsFacadeInvoker;
    }

    @Override
    public Validator<AccountCreateRequest> getRequestValidator() {
        return new AccountCreateRequestValidator();
    }

    @Override
    public AccountCreateResponse handleRequest(AccountCreateRequest accountCreateRequest) throws Exception {
        Account account = bankActionsFacadeInvoker.createAccount(accountCreateRequest.getInitialDeposit());
        ModelApiResponse status = new ModelApiResponse().code(ACCOUNT_CREATED);

        return new AccountCreateResponse()
                .status(status)
                .account(account);
    }
}
