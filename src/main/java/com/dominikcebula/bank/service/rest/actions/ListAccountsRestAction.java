package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacade;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.google.inject.Inject;

public class ListAccountsRestAction extends AbstractRestAction<Void, AccountsInfo> {

    public static final String ACCOUNT_LIST_URI = "/accounts/list";

    private final BankActionsFacade bankActionsFacade;

    @Inject
    public ListAccountsRestAction(GsonProvider gsonProvider, BankActionsFacade bankActionsFacade) {
        super(gsonProvider, Void.class, AccountsInfo.class);
        this.bankActionsFacade = bankActionsFacade;
    }

    @Override
    AccountsInfo handleRequest(Void request) {
        return bankActionsFacade.listAccounts();
    }
}
