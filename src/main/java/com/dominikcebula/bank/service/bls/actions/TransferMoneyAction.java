package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountMissingException;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.bls.exception.WithdrawException;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.logging.Loggers;
import com.google.inject.Inject;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

class TransferMoneyAction {

    private Logger logger = LoggerFactory.getLogger(Loggers.BLS);

    private final AccountDao accountDao;
    private final MoneyFactory moneyFactory;

    @Inject
    TransferMoneyAction(AccountDao accountDao, MoneyFactory moneyFactory) {
        this.accountDao = accountDao;
        this.moneyFactory = moneyFactory;
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
        Money accountBalanceMoney = moneyFactory.create(accountBalance);
        Money amountMoney = moneyFactory.create(amount);

        if (accountBalanceMoney.isGreaterThanOrEqualTo(amountMoney))
            return accountBalanceMoney.subtract(amountMoney).getNumberStripped();
        else
            throw new WithdrawException(String.format("Unable to withdraw amount [%s] from account that has balance [%s]", amount, accountBalance));
    }

    private BigDecimal deposit(BigDecimal accountBalance, BigDecimal amount) {
        Money accountBalanceMoney = moneyFactory.create(accountBalance);
        Money amountMoney = moneyFactory.create(amount);

        return accountBalanceMoney.add(amountMoney).getNumberStripped();
    }
}
