package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import org.javamoney.moneta.Money;

abstract class BankActionsFacadeIfc {

    abstract AccountId openAccount(Money initialBalance) throws AccountOpenException;

    abstract void transfer(AccountId from, AccountId to, Money amount) throws TransferException;

    abstract AccountsInfo listAccounts();
}
