package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.dto.Accounts;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.google.inject.Inject;

public class ListAccountsRestAction extends AbstractRestAction<Void, Accounts> {

    public static final String ACCOUNT_LIST_URI = "/accounts/list";

    private final BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Inject
    public ListAccountsRestAction(GsonProvider gsonProvider, BankActionsFacadeInvoker bankActionsFacadeInvoker) {
        super(gsonProvider, Void.class, Accounts.class);
        this.bankActionsFacadeInvoker = bankActionsFacadeInvoker;
    }

    @Override
    Accounts handleRequest(Void request) {
        return bankActionsFacadeInvoker.listAccounts();
    }
}
