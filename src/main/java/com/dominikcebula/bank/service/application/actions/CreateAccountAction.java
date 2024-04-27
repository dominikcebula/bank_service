package com.dominikcebula.bank.service.application.actions;

import com.dominikcebula.bank.service.application.dao.AccountDao;
import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.application.utils.AccountIdGenerator;
import com.dominikcebula.bank.service.application.utils.MoneyAmountRound;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.logging.Loggers;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

class CreateAccountAction {

    private final Logger logger = LoggerFactory.getLogger(Loggers.APPLICATION);

    private final AccountDao accountDao;
    private final AccountIdGenerator accountIdGenerator;
    private final MoneyAmountRound moneyAmountRound;

    @Inject
    @SuppressWarnings("unused")
    CreateAccountAction(AccountDao accountDao, AccountIdGenerator accountIdGenerator, MoneyAmountRound moneyAmountRound) {
        this.accountDao = accountDao;
        this.accountIdGenerator = accountIdGenerator;
        this.moneyAmountRound = moneyAmountRound;
    }

    Account createAccount(BigDecimal initialBalance) throws InterruptedException {
        logger.info("Creating account");

        return accountDao.runInReadWriteTransaction(() -> {
            logger.info("Generating account id and creating account");
            AccountId accountId = accountIdGenerator.generateAccountId(accountDao.findAccountIdentifiers());
            Account account = new Account();
            account.setAccountId(accountId.getAccountNumber());
            account.setBalance(moneyAmountRound.round(initialBalance));

            logger.info("Saving account");
            accountDao.store(account);

            return account;
        });
    }
}
