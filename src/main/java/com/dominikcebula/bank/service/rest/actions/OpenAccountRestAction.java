package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.AccountOpenRequest;
import com.dominikcebula.bank.service.dto.AccountOpenResponse;
import com.dominikcebula.bank.service.dto.ModelApiResponse;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.AccountOpenRequestValidator;
import com.google.inject.Inject;

import static com.dominikcebula.bank.service.dto.ApiCode.ACCOUNT_OPENED;

public class OpenAccountRestAction extends AbstractValidatingRestAction<AccountOpenRequest, AccountOpenResponse> {

    public static final String ACCOUNTS_OPEN_URI = "/accounts/open";

    private final BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Inject
    public OpenAccountRestAction(BankActionsFacadeInvoker bankActionsFacadeInvoker) {
        super(AccountOpenRequest.class, AccountOpenResponse.class);
        this.bankActionsFacadeInvoker = bankActionsFacadeInvoker;
    }

    @Override
    public Validator<AccountOpenRequest> getRequestValidator() {
        return new AccountOpenRequestValidator();
    }

    @Override
    public AccountOpenResponse handleRequest(AccountOpenRequest accountOpenRequest) throws Exception {
        Account account = bankActionsFacadeInvoker.openAccount(accountOpenRequest.getInitialDeposit());
        ModelApiResponse status = new ModelApiResponse().code(ACCOUNT_OPENED);

        return new AccountOpenResponse()
                .status(status)
                .account(account);
    }
}
