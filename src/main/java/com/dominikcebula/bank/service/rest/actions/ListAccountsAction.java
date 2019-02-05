package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.ActionsFacade;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;

public class ListAccountsAction extends AbstractRestAction<Void, AccountsInfo> {

    private final ActionsFacade actionsFacade;

    public ListAccountsAction(ActionsFacade actionsFacade) {
        super(Void.class, AccountsInfo.class);
        this.actionsFacade = actionsFacade;
    }

    @Override
    AccountsInfo handleRequest(Void request) {
        return actionsFacade.listAccounts();
    }
}
