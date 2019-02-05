package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.bls.ds.Account;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountMissingException;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.bls.exception.WithDrawException;
import org.javamoney.moneta.Money;

public class TransferMoneyAction {

    private final AccountDao accountDao;

    public TransferMoneyAction(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    void transfer(AccountId from, AccountId to, Money amount) throws TransferException {
        try {
            Account fromAccount = getExistingAccountForTransfer(from);
            Account toAccount = getExistingAccountForTransfer(to);

            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
        } catch (WithDrawException | AccountMissingException e) {
            throw new TransferException(String.format("Unable to transfer amount = [%s] from = [%s] to = [%s]: %s", amount, from, to, e.getMessage()), e);
        }
    }

    private Account getExistingAccountForTransfer(AccountId accountId) throws AccountMissingException {
        if (accountDao.accountExists(accountId))
            return accountDao.findAccount(accountId);
        else
            throw new AccountMissingException(String.format("Unable to locate account [%s]", accountId));
    }
}
