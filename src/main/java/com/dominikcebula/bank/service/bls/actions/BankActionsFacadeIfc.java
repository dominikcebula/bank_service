package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountCreateException;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;

import java.math.BigDecimal;

abstract class BankActionsFacadeIfc {

    abstract Account createAccount(BigDecimal initialBalance) throws AccountCreateException, InterruptedException;

    abstract void transfer(AccountId from, AccountId to, BigDecimal amount) throws TransferException;

    abstract Accounts listAccounts();
}
