package com.dominikcebula.bank.service.application.actions;

import com.dominikcebula.bank.service.application.dao.AccountDao;
import com.dominikcebula.bank.service.application.utils.MoneyAmountRound;
import com.dominikcebula.bank.service.application.utils.MoneyCalculator;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;
import com.dominikcebula.bank.service.logging.Loggers;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;

class ListAccountsAction {

    private final Logger logger = LogManager.getLogger(Loggers.APPLICATION);

    private final AccountDao accountDao;
    private final MoneyCalculator moneyCalculator;
    private final MoneyAmountRound moneyAmountRound;

    @Inject
    @SuppressWarnings("unused")
    ListAccountsAction(AccountDao accountDao, MoneyCalculator moneyCalculator, MoneyAmountRound moneyAmountRound) {
        this.accountDao = accountDao;
        this.moneyCalculator = moneyCalculator;
        this.moneyAmountRound = moneyAmountRound;
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
                .reduce(moneyCalculator::add)
                .orElse(moneyAmountRound.round(BigDecimal.ZERO));
    }
}
