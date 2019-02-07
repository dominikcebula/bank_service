package com.dominikcebula.bank.service.bls.dao;

import com.dominikcebula.bank.service.bls.ds.Account;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.ds.AccountInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDao {
    private final Map<AccountId, Account> accounts = new HashMap<>();

    public Collection<Account> findAllAccounts() {
        return accounts.values().stream()
                .map(Account::new)
                .collect(Collectors.toList());
    }

    public Set<AccountInfo> findAllAccountsInfo() {
        return accounts.entrySet().stream()
                .map(this::getAccountInfo)
                .collect(Collectors.toSet());
    }

    public Account findAccount(AccountId accountId) {
        Account account = accounts.get(accountId);
        if (account != null)
            return new Account(account);
        else
            return null;
    }

    public Set<AccountId> findAccountIdentifiers() {
        return accounts.keySet();
    }

    public boolean accountExists(AccountId accountId) {
        return accounts.containsKey(accountId);
    }

    public void store(AccountId accountId, Account account) {
        accounts.put(accountId, account);
    }

    private AccountInfo getAccountInfo(Map.Entry<AccountId, Account> accountEntry) {
        return new AccountInfo(accountEntry.getKey(), accountEntry.getValue().getBalance());
    }
}
