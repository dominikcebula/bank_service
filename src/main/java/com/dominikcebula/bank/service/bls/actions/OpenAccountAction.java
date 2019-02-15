package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;
import com.dominikcebula.bank.service.bls.utils.AccountIdGenerator;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.logging.Loggers;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

class OpenAccountAction {

    private Logger logger = LoggerFactory.getLogger(Loggers.BLS);

    private final AccountDao accountDao;
    private final AccountIdGenerator accountIdGenerator;

    @Inject
    OpenAccountAction(AccountDao accountDao, AccountIdGenerator accountIdGenerator) {
        this.accountDao = accountDao;
        this.accountIdGenerator = accountIdGenerator;
    }

    Account openAccount(BigDecimal initialBalance) throws AccountOpenException {
        logger.info("Opening account");

        logger.info("Generating account id and creating account");
        AccountId accountId = accountIdGenerator.generateAccountId(accountDao.findAccountIdentifiers());
        Account account = new Account();
        account.setAccountId(accountId.getAccountNumber());
        account.setBalance(initialBalance);

        logger.info("Saving account");
        accountDao.store(account);

        return account;
    }
}
