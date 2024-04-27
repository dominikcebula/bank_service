package com.dominikcebula.bank.service.application.actions;

import com.dominikcebula.bank.service.application.dao.AccountDao;
import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.application.exception.AccountLockException;
import com.dominikcebula.bank.service.application.exception.AccountMissingException;
import com.dominikcebula.bank.service.application.exception.TransferException;
import com.dominikcebula.bank.service.application.exception.WithdrawException;
import com.dominikcebula.bank.service.application.utils.MoneyCalculator;
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

    void transfer(AccountId from, AccountId to, BigDecimal amount) {
        try {
            logger.info(String.format("Transferring [%s] amount from [%s] to [%s]", from, to, amount));

            logger.info("Validating that accounts exists");
            validateAccountExists(from);
            validateAccountExists(to);

            logger.info("Trying to lock accounts");
            tryLockAccount(from);
            try {
                tryLockAccount(to);
                try {
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
                    store(fromAccount);
                    store(toAccount);
                } finally {
                    unlockAccount(to);
                }
            } finally {
                unlockAccount(from);
            }
        } catch (WithdrawException | AccountMissingException | AccountLockException e) {
            throw new TransferException(String.format("Unable to transfer amount [%s] from [%s] to [%s]: %s", amount, from, to, e.getMessage()), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TransferException(String.format("Money transfer of amount [%s] from [%s] to [%s] was interrupted: %s", amount, from, to, e.getMessage()), e);
        }
    }

    private void validateAccountExists(AccountId accountId) {
        if (!accountDao.accountExists(accountId))
            throw new AccountMissingException(String.format("Unable to locate account [%s]", accountId));
    }

    private void tryLockAccount(AccountId accountId) throws InterruptedException {
        accountDao.tryLockAccount(accountId);
    }

    private void unlockAccount(AccountId accountId) {
        accountDao.unlockAccount(accountId);
    }

    private Account getExistingAccountForTransfer(AccountId accountId) {
        validateAccountExists(accountId);
        return accountDao.findAccount(accountId);
    }

    private BigDecimal withdraw(BigDecimal accountBalance, BigDecimal amount) {
        if (accountBalance.compareTo(amount) >= 0)
            return moneyCalculator.subtract(accountBalance, amount);
        else
            throw new WithdrawException(String.format("Unable to withdraw amount [%s] from account that has balance [%s]", amount, accountBalance));
    }

    private BigDecimal deposit(BigDecimal accountBalance, BigDecimal amount) {
        return moneyCalculator.add(accountBalance, amount);
    }

    private void store(Account toAccount) throws InterruptedException {
        accountDao.store(toAccount);
    }
}
