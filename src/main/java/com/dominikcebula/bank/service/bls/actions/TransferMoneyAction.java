package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountMissingException;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.bls.exception.WithdrawException;
import com.dominikcebula.bank.service.bls.utils.MoneyCalculator;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.logging.Loggers;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

class TransferMoneyAction {

    private final Logger logger = LoggerFactory.getLogger(Loggers.BLS);

    private final AccountDao accountDao;
    private final MoneyCalculator moneyCalculator;

    @Inject
    @SuppressWarnings("unused")
    TransferMoneyAction(AccountDao accountDao, MoneyCalculator moneyCalculator) {
        this.accountDao = accountDao;
        this.moneyCalculator = moneyCalculator;
    }

    void transfer(AccountId from, AccountId to, BigDecimal amount) throws TransferException {
        try {
            logger.info(String.format("Transferring [%s] amount from [%s] to [%s]", from, to, amount));

            logger.info("Fetching accounts");
            Account fromAccount = getExistingAccountForTransfer(from);
            Account toAccount = getExistingAccountForTransfer(to);

            logger.info("Performing transfer");
            fromAccount.setBalance(
                    withdraw(fromAccount.getBalance(), amount)
            );

            toAccount.setBalance(
                    deposit(toAccount.getBalance(), amount)
            );

            logger.info("Saving new accounts balance");
            accountDao.store(fromAccount);
            accountDao.store(toAccount);

        } catch (WithdrawException | AccountMissingException e) {
            throw new TransferException(String.format("Unable to transfer amount [%s] from [%s] to [%s]: %s", amount, from, to, e.getMessage()), e);
        }
    }

    private Account getExistingAccountForTransfer(AccountId accountId) throws AccountMissingException {
        if (accountDao.accountExists(accountId))
            return accountDao.findAccount(accountId);
        else
            throw new AccountMissingException(String.format("Unable to locate account [%s]", accountId));
    }

    private BigDecimal withdraw(BigDecimal accountBalance, BigDecimal amount) throws WithdrawException {
        if (accountBalance.compareTo(amount) >= 0)
            return moneyCalculator.subtract(accountBalance, amount);
        else
            throw new WithdrawException(String.format("Unable to withdraw amount [%s] from account that has balance [%s]", amount, accountBalance));
    }

    private BigDecimal deposit(BigDecimal accountBalance, BigDecimal amount) {
        return moneyCalculator.add(accountBalance, amount);
    }
}
