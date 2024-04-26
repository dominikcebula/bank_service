package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.application.actions.BankActionsFacade;
import com.dominikcebula.bank.service.dto.Accounts;
import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ListAccountsResponse;
import com.dominikcebula.bank.service.dto.ModelApiResponse;
import com.google.inject.Inject;

public class ListAccountsRestAction extends AbstractRestAction<Void, ListAccountsResponse> {

    public static final String ACCOUNT_LIST_URI = "/accounts";

    private final BankActionsFacade bankActionsFacade;

    @Inject
    @SuppressWarnings("unused")
    public ListAccountsRestAction(BankActionsFacade bankActionsFacade) {
        super(Void.class, ListAccountsResponse.class);
        this.bankActionsFacade = bankActionsFacade;
    }

    @Override
    ListAccountsResponse handleRequest(Void request) {
        Accounts accounts = bankActionsFacade.listAccounts();

        return new ListAccountsResponse()
                .status(new ModelApiResponse().code(ApiCode.ACCOUNTS_LISTED))
                .accounts(accounts);
    }
}
