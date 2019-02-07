package com.dominikcebula.bank.service.bls.ds;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

@Getter
@Setter
@EqualsAndHashCode
public class Account {
    private Money balance;

    public Account(Money balance) {
        this.balance = balance;
    }

    public Account(Account other) {
        this.balance = other.getBalance();
    }
}
