package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.bls.registry.AccountRegistry;

public class ListAccountsAction extends AbstractRestAction<Void, AccountsInfo> {

    private final AccountRegistry accountRegistry = AccountRegistry.getInstance();

    public ListAccountsAction() {
        super(Void.class, AccountsInfo.class);
    }

    @Override
    AccountsInfo handleRequest(Void request) {
        return accountRegistry.listAccounts();
    }
}
