package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.bls.ds.Account;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.logging.Loggers;
import com.google.inject.Inject;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ListAccountsAction {

    private Logger logger = LoggerFactory.getLogger(Loggers.BLS);

    private final AccountDao accountDao;
    private final MoneyFactory moneyFactory;

    @Inject
    ListAccountsAction(AccountDao accountDao, MoneyFactory moneyFactory) {
        this.accountDao = accountDao;
        this.moneyFactory = moneyFactory;
    }

    AccountsInfo listAccounts() {
        logger.info("Fetching list of accounts");

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
