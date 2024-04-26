package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountCreateException;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;
import com.google.inject.Inject;

import java.math.BigDecimal;

public class BankActionsFacade extends BankActionsFacadeIfc {

    private final CreateAccountAction createAccountAction;
    private final TransferMoneyAction transferMoneyAction;
    private final ListAccountsAction listAccountsAction;

    @Inject
    BankActionsFacade(CreateAccountAction createAccountAction, TransferMoneyAction transferMoneyAction, ListAccountsAction listAccountsAction) {
        this.createAccountAction = createAccountAction;
        this.transferMoneyAction = transferMoneyAction;
        this.listAccountsAction = listAccountsAction;
    }

    @Override
    Account createAccount(BigDecimal initialBalance) throws AccountCreateException, InterruptedException {
        return createAccountAction.createAccount(initialBalance);
    }

    @Override
    void transfer(AccountId from, AccountId to, BigDecimal amount) throws TransferException {
        transferMoneyAction.transfer(from, to, amount);
    }

    @Override
    Accounts listAccounts() {
        return listAccountsAction.listAccounts();
    }
}
