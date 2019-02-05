package com.dominikcebula.bank.service.bls.ds;

import com.dominikcebula.bank.service.bls.exception.WithDrawException;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

@Getter
@Setter
public class Account {
    private final Money balance;

    public Account(Money balance) {
        this.balance = balance;
    }

    public void deposit(Money amount) {
        balance.add(amount);
    }

    public void withdraw(Money amount) throws WithDrawException {
        if (balance.isGreaterThanOrEqualTo(amount))
            balance.subtract(amount);
        else
            throw new WithDrawException(String.format("Unable to withdraw amount = [%s] from account that has balance = [%s]", amount, balance));
    }
}
