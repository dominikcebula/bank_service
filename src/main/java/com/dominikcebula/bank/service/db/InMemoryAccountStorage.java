package com.dominikcebula.bank.service.db;

import com.dominikcebula.bank.service.application.dao.AccountDao;
import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.application.exception.AccountLockException;
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

import static lombok.AccessLevel.PACKAGE;

@RequiredArgsConstructor(onConstructor_ = @__({@Inject}))
public class InMemoryAccountStorage implements AccountDao {
    private final Map<AccountId, LockableAccount> accounts = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private final Configuration configuration;

    @Override
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

    @Override
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

    @Override
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

    @Override
    public boolean accountExists(AccountId accountId) {
        try {
            readLock.lock();

            return accounts.containsKey(accountId);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void store(Account account) throws InterruptedException {
        try {
            writeLock.lock();

            AccountId accountNumber = AccountId.createAccountNumber(account.getAccountId());

            if (!accountExists(accountNumber)) {
                createAccount(accountNumber, account);
            } else {
                updateAccount(accountNumber, account);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void tryLockAccount(AccountId accountId) throws InterruptedException {
        LockableAccount lockableAccount = accounts.get(accountId);
        tryLockAccount(lockableAccount);
    }

    @Override
    public void unlockAccount(AccountId accountId) {
        LockableAccount lockableAccount = accounts.get(accountId);
        unlockAccount(lockableAccount);
    }

    private Account copyAccount(Account account) {
        Account newAccount = new Account();
        newAccount.setAccountId(account.getAccountId());
        newAccount.setBalance(account.getBalance());
        return newAccount;
    }

    private void createAccount(AccountId accountNumber, Account account) {
        accounts.put(accountNumber, new LockableAccount(account));
    }

    private void updateAccount(AccountId accountNumber, Account account) throws InterruptedException {
        LockableAccount existingAccount = accounts.get(accountNumber);
        tryLockAccount(existingAccount);

        try {
            existingAccount.setAccount(account);
        } finally {
            unlockAccount(existingAccount);
        }
    }

    private void tryLockAccount(LockableAccount lockableAccount) throws InterruptedException {
        boolean lockAccountResult = lockableAccount.tryLock(configuration.getAccountWaitForLockMaxTimeMillis(), TimeUnit.MILLISECONDS);

        if (!lockAccountResult)
            throw new AccountLockException(AccountId.createAccountNumber(lockableAccount.getAccount().getAccountId()));
    }

    private void unlockAccount(LockableAccount lockableAccount) {
        lockableAccount.unlock();
    }

    @AllArgsConstructor
    private static class LockableAccount {
        private final Lock lock = new ReentrantLock();

        @Setter(PACKAGE)
        @Getter(PACKAGE)
        private Account account;

        boolean tryLock(long timeout, TimeUnit timeUnit) throws InterruptedException {
            return lock.tryLock(timeout, timeUnit);
        }

        void unlock() {
            lock.unlock();
        }
    }
}
