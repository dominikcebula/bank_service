package com.dominikcebula.bank.service.bls.dao;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.dto.Account;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @__({@Inject}))
public class AccountDao {
    private final Map<AccountId, LockableAccount> accounts = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private final Configuration configuration;

    public List<Account> findAllAccounts() {
        try {
            readLock.lock();

            return accounts.values().stream()
                    .map(LockableAccount::getAccount)
                    .map(this::copyAccount)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    public Account findAccount(AccountId accountId) {
        try {
            readLock.lock();

            LockableAccount lockableAccount = accounts.get(accountId);
            if (lockableAccount != null) {
                Account account = lockableAccount.getAccount();
                if (account != null)
                    return copyAccount(account);
                else
                    return null;
            } else {
                return null;
            }
        } finally {
            readLock.unlock();
        }
    }

    public Set<AccountId> findAccountIdentifiers() {
        try {
            readLock.lock();

            return accounts.keySet().stream()
                    .map(AccountId::getAccountNumber)
                    .map(AccountId::createAccountNumber)
                    .collect(Collectors.toSet());
        } finally {
            readLock.unlock();
        }
    }

    public boolean accountExists(AccountId accountId) {
        try {
            readLock.lock();

            return accounts.containsKey(accountId);
        } finally {
            readLock.unlock();
        }
    }

    public void store(Account account) throws InterruptedException {
        try {
            writeLock.lock();

            AccountId accountNumber = AccountId.createAccountNumber(account.getAccountId());

            if (accountExists(accountNumber)) {
                LockableAccount existingAccount = accounts.get(accountNumber);
                existingAccount.tryLock(configuration.getAccountWaitForLockMaxTimeMillis(), TimeUnit.MILLISECONDS);
                try {
                    existingAccount.setAccount(account);
                } finally {
                    existingAccount.unlock();
                }
            } else {
                accounts.put(accountNumber, new LockableAccount(account));
            }
        } finally {
            writeLock.unlock();
        }
    }

    public boolean tryLockAccount(AccountId accountId) throws InterruptedException {
        LockableAccount lockableAccount = accounts.get(accountId);
        return lockableAccount.tryLock(configuration.getAccountWaitForLockMaxTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public void unlockAccount(AccountId accountId) {
        LockableAccount lockableAccount = accounts.get(accountId);
        lockableAccount.unlock();
    }

    private Account copyAccount(Account account) {
        Account newAccount = new Account();
        newAccount.setAccountId(account.getAccountId());
        newAccount.setBalance(account.getBalance());
        return newAccount;
    }

    @AllArgsConstructor
    private static class LockableAccount {
        private final Lock lock = new ReentrantLock();

        @Setter
        @Getter
        private Account account;

        public boolean tryLock(long timeout, TimeUnit timeUnit) throws InterruptedException {
            return lock.tryLock(timeout, timeUnit);
        }

        public void unlock() {
            lock.unlock();
        }
    }
}
