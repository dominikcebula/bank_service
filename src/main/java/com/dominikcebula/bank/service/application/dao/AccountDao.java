package com.dominikcebula.bank.service.application.dao;

import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.dto.Account;

import java.util.List;
import java.util.Set;

public interface AccountDao {
    List<Account> findAllAccounts();

    Set<AccountId> findAccountIdentifiers();

    Account findAccount(AccountId accountId);

    boolean accountExists(AccountId accountId);

    void store(Account account) throws InterruptedException;

    <R> R runInReadWriteTransaction(ReadWriteTransactionOperation<R> operation) throws InterruptedException;

    void tryLockAccount(AccountId accountId) throws InterruptedException;

    void unlockAccount(AccountId accountId);

    @FunctionalInterface
    interface ReadWriteTransactionOperation<R> {
        R execute() throws InterruptedException;
    }
}
