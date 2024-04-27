package com.dominikcebula.bank.service.application.actions;

import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;
import com.google.inject.Inject;

import java.math.BigDecimal;

public class BankActionsFacade {

    private final CreateAccountAction createAccountAction;
    private final TransferMoneyAction transferMoneyAction;
    private final ListAccountsAction listAccountsAction;

    @Inject
    BankActionsFacade(CreateAccountAction createAccountAction, TransferMoneyAction transferMoneyAction, ListAccountsAction listAccountsAction) {
        this.createAccountAction = createAccountAction;
        this.transferMoneyAction = transferMoneyAction;
        this.listAccountsAction = listAccountsAction;
    }

    public Account createAccount(BigDecimal initialBalance) throws InterruptedException {
        return createAccountAction.createAccount(initialBalance);
    }

    public void transfer(AccountId from, AccountId to, BigDecimal amount) {
        transferMoneyAction.transfer(from, to, amount);
    }

    public Accounts listAccounts() {
        return listAccountsAction.listAccounts();
    }
}
