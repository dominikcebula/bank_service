package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacade;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.google.gson.Gson;

public class ListAccountsRestAction extends AbstractRestAction<Void, AccountsInfo> {

    private final BankActionsFacade bankActionsFacade;

    public ListAccountsRestAction(Gson gson, BankActionsFacade bankActionsFacade) {
        super(gson, Void.class, AccountsInfo.class);
        this.bankActionsFacade = bankActionsFacade;
    }

    @Override
    AccountsInfo handleRequest(Void request) {
        return bankActionsFacade.listAccounts();
    }
}
