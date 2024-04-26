package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.dto.Accounts;
import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ListAccountsResponse;
import com.dominikcebula.bank.service.dto.ModelApiResponse;
import com.google.inject.Inject;

public class ListAccountsRestAction extends AbstractRestAction<Void, ListAccountsResponse> {

    public static final String ACCOUNT_LIST_URI = "/accounts";

    private final BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Inject
    @SuppressWarnings("unused")
    public ListAccountsRestAction(BankActionsFacadeInvoker bankActionsFacadeInvoker) {
        super(Void.class, ListAccountsResponse.class);
        this.bankActionsFacadeInvoker = bankActionsFacadeInvoker;
    }

    @Override
    ListAccountsResponse handleRequest(Void request) {
        Accounts accounts = bankActionsFacadeInvoker.listAccounts();

        return new ListAccountsResponse()
                .status(new ModelApiResponse().code(ApiCode.ACCOUNTS_LISTED))
                .accounts(accounts);
    }
}
