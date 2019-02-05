package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.bls.ds.Account;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountMissingException;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.bls.exception.WithdrawException;
import com.google.inject.Inject;
import org.javamoney.moneta.Money;

class TransferMoneyAction {

    private final AccountDao accountDao;

    @Inject
    TransferMoneyAction(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    void transfer(AccountId from, AccountId to, Money amount) throws TransferException {
        try {
            Account fromAccount = getExistingAccountForTransfer(from);
            Account toAccount = getExistingAccountForTransfer(to);

            fromAccount.setBalance(
                    withdraw(fromAccount.getBalance(), amount)
            );

            toAccount.setBalance(
                    deposit(toAccount.getBalance(), amount)
            );

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

    private Money withdraw(Money accountBalance, Money amount) throws WithdrawException {
        if (accountBalance.isGreaterThanOrEqualTo(amount))
            return accountBalance.subtract(amount);
        else
            throw new WithdrawException(String.format("Unable to withdraw amount [%s] from account that has balance [%s]", amount, accountBalance));
    }

    private Money deposit(Money accountBalance, Money amount) {
        return accountBalance.add(amount);
    }
}
