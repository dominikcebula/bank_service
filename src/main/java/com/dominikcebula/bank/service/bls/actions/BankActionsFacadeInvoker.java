package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;
import com.google.inject.Inject;

import java.math.BigDecimal;

public class BankActionsFacadeInvoker extends BankActionsFacadeIfc {

    private final BankActionsFacade bankActionsFacade;

    @Inject
    public BankActionsFacadeInvoker(BankActionsFacade bankActionsFacade) {
        this.bankActionsFacade = bankActionsFacade;
    }

    @Override
    public Account openAccount(BigDecimal initialBalance) throws AccountOpenException {
        return bankActionsFacade.openAccount(initialBalance);
    }

    @Override
    public void transfer(AccountId from, AccountId to, BigDecimal amount) throws TransferException {
        bankActionsFacade.transfer(from, to, amount);
    }

    @Override
    public Accounts listAccounts() {
        return bankActionsFacade.listAccounts();
    }
}
