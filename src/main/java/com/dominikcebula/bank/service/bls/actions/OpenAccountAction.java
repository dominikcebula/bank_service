package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.bls.ds.Account;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;
import com.dominikcebula.bank.service.bls.utils.AccountIdGenerator;
import com.google.inject.Inject;
import org.javamoney.moneta.Money;

class OpenAccountAction {

    private final AccountDao accountDao;
    private final AccountIdGenerator accountIdGenerator;

    @Inject
    public OpenAccountAction(AccountDao accountDao, AccountIdGenerator accountIdGenerator) {
        this.accountDao = accountDao;
        this.accountIdGenerator = accountIdGenerator;
    }

    AccountId openAccount(Money initialBalance) throws AccountOpenException {
        AccountId accountId = accountIdGenerator.generateAccountId(accountDao.findAccountIdentifiers());
        Account account = new Account(initialBalance);

        accountDao.store(accountId, account);

        return accountId;
    }
}
