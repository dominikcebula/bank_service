package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;
import com.dominikcebula.bank.service.logging.Loggers;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

class ListAccountsAction {

    private Logger logger = LoggerFactory.getLogger(Loggers.BLS);

    private final AccountDao accountDao;

    @Inject
    ListAccountsAction(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    Accounts listAccounts() {
        logger.info("Fetching list of accounts");

        List<Account> accountList = accountDao.findAllAccounts();
        BigDecimal totalDeposit = getTotalDeposit(accountList);

        return new Accounts()
                .accounts(accountList)
                .totalDeposit(totalDeposit);
    }

    private BigDecimal getTotalDeposit(List<Account> accountList) {
        return accountList.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
