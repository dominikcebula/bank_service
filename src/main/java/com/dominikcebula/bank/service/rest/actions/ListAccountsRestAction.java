package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.google.inject.Inject;

public class ListAccountsRestAction extends AbstractRestAction<Void, AccountsInfo> {

    public static final String ACCOUNT_LIST_URI = "/accounts/list";

    private final BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Inject
    public ListAccountsRestAction(GsonProvider gsonProvider, BankActionsFacadeInvoker bankActionsFacadeInvoker) {
        super(gsonProvider, Void.class, AccountsInfo.class);
        this.bankActionsFacadeInvoker = bankActionsFacadeInvoker;
    }

    @Override
    AccountsInfo handleRequest(Void request) {
        return bankActionsFacadeInvoker.listAccounts();
    }
}
