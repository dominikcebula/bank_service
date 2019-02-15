package com.dominikcebula.bank.service.bls.dao;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.dto.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDao {
    private final Map<AccountId, Account> accounts = new HashMap<>();

    public List<Account> findAllAccounts() {
        return accounts.values().stream()
                .map(this::copyAccount)
                .collect(Collectors.toList());
    }

    public Account findAccount(AccountId accountId) {
        Account account = accounts.get(accountId);
        if (account != null)
            return copyAccount(account);
        else
            return null;
    }

    public Set<AccountId> findAccountIdentifiers() {
        return accounts.keySet();
    }

    public boolean accountExists(AccountId accountId) {
        return accounts.containsKey(accountId);
    }

    public void store(Account account) {
        accounts.put(AccountId.createAccountNumber(account.getAccountId()), account);
    }

    private Account copyAccount(Account account) {
        Account newAccount = new Account();
        newAccount.setAccountId(account.getAccountId());
        newAccount.setBalance(account.getBalance());
        return account;
    }
}
