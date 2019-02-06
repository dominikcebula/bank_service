package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.google.inject.Inject;
import org.javamoney.moneta.Money;

public class BankActionsFacadeInvoker extends BankActionsFacadeIfc {

    private final BankActionsFacade bankActionsFacade;

    @Inject
    public BankActionsFacadeInvoker(BankActionsFacade bankActionsFacade) {
        this.bankActionsFacade = bankActionsFacade;
    }

    @Override
    public AccountId openAccount(Money initialBalance) throws AccountOpenException {
        return bankActionsFacade.openAccount(initialBalance);
    }

    @Override
    public void transfer(AccountId from, AccountId to, Money amount) throws TransferException {
        bankActionsFacade.transfer(from, to, amount);
    }

    @Override
    public AccountsInfo listAccounts() {
        return bankActionsFacade.listAccounts();
    }
}
