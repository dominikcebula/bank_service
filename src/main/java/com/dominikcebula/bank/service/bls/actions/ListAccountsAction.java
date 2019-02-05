package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.bls.ds.Account;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import org.javamoney.moneta.Money;

public class ListAccountsAction {

    private final AccountDao accountDao;
    private final MoneyFactory moneyFactory;

    public ListAccountsAction(AccountDao accountDao, MoneyFactory moneyFactory) {
        this.accountDao = accountDao;
        this.moneyFactory = moneyFactory;
    }

    AccountsInfo listAccounts() {
        return new AccountsInfo(
                accountDao.findAllAccountsInfo(),
                getTotalBalance()
        );
    }

    private Money getTotalBalance() {
        return accountDao.findAllAccounts().stream()
                .map(Account::getBalance)
                .reduce(Money::add)
                .orElseGet(() -> moneyFactory.create(0));
    }
}
