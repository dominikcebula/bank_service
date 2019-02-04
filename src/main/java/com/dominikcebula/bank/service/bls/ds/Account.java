package com.dominikcebula.bank.service.bls.ds;

import lombok.Getter;
import org.javamoney.moneta.Money;

@Getter
public class Account {
    private final Money balance;

    public Account(Money balance) {
        this.balance = balance;
    }

    public void deposit(Money amount) {
        balance.add(amount);
    }

    public void withdraw(Money amount) {
        balance.subtract(amount);
    }
}
