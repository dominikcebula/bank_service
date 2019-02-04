package com.dominikcebula.bank.service.bls.registry;

import com.dominikcebula.bank.service.bls.ds.Account;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.ds.AccountInfo;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;
import com.dominikcebula.bank.service.bls.utils.AccountIdGenerator;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import org.javamoney.moneta.Money;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountRegistry {
    private static final AccountRegistry INSTANCE = new AccountRegistry();

    private final Map<AccountId, Account> accounts = new HashMap<>();

    private final AccountIdGenerator accountIdGenerator = new AccountIdGenerator();
    private final MoneyFactory moneyFactory = new MoneyFactory();

    public static AccountRegistry getInstance() {
        return INSTANCE;
    }

    public synchronized AccountId openAccount(Money initialBalance) throws AccountOpenException {
        AccountId accountId = accountIdGenerator.generateAccountId(accounts.keySet());
        Account account = new Account(initialBalance);

        accounts.put(accountId, account);

        return accountId;
    }

    public synchronized AccountsInfo listAccounts() {
        return new AccountsInfo(
                getAccountsInfo(),
                getTotalBalance()
        );
    }

    private Set<AccountInfo> getAccountsInfo() {
        return accounts.entrySet().stream()
                .map(this::getAccountInfo)
                .collect(Collectors.toSet());
    }

    private AccountInfo getAccountInfo(Map.Entry<AccountId, Account> accountEntry) {
        return new AccountInfo(accountEntry.getKey(), accountEntry.getValue().getBalance());
    }

    private Money getTotalBalance() {
        return accounts.values().stream()
                .map(Account::getBalance)
                .reduce(Money::add)
                .orElseGet(() -> moneyFactory.create(0));
    }
}
