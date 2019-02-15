package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;
import com.google.inject.Inject;
import org.javamoney.moneta.Money;

import java.math.BigDecimal;

public class BankActionsFacade extends BankActionsFacadeIfc {

    private final OpenAccountAction openAccountAction;
    private final TransferMoneyAction transferMoneyAction;
    private final ListAccountsAction listAccountsAction;

    @Inject
    BankActionsFacade(OpenAccountAction openAccountAction, TransferMoneyAction transferMoneyAction, ListAccountsAction listAccountsAction) {
        this.openAccountAction = openAccountAction;
        this.transferMoneyAction = transferMoneyAction;
        this.listAccountsAction = listAccountsAction;
    }

    @Override
    synchronized Account openAccount(BigDecimal initialBalance) throws AccountOpenException {
        return openAccountAction.openAccount(initialBalance);
    }

    @Override
    synchronized void transfer(AccountId from, AccountId to, BigDecimal amount) throws TransferException {
        transferMoneyAction.transfer(from, to, amount);
    }

    @Override
    synchronized Accounts listAccounts() {
        return listAccountsAction.listAccounts();
    }
}
