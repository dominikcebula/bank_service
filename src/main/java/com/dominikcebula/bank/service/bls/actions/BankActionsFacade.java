package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.google.inject.Inject;
import org.javamoney.moneta.Money;

public class BankActionsFacade {

    private final OpenAccountAction openAccountAction;
    private final TransferMoneyAction transferMoneyAction;
    private final ListAccountsAction listAccountsAction;

    @Inject
    public BankActionsFacade(OpenAccountAction openAccountAction, TransferMoneyAction transferMoneyAction, ListAccountsAction listAccountsAction) {
        this.openAccountAction = openAccountAction;
        this.transferMoneyAction = transferMoneyAction;
        this.listAccountsAction = listAccountsAction;
    }

    public synchronized AccountId openAccount(Money initialBalance) throws AccountOpenException {
        return openAccountAction.openAccount(initialBalance);
    }

    public synchronized void transfer(AccountId from, AccountId to, Money amount) throws TransferException {
        transferMoneyAction.transfer(from, to, amount);
    }

    public synchronized AccountsInfo listAccounts() {
        return listAccountsAction.listAccounts();
    }
}
