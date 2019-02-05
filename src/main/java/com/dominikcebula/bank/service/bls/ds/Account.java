package com.dominikcebula.bank.service.bls.ds;

import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

@Getter
@Setter
public class Account {
    private Money balance;

    public Account(Money balance) {
        this.balance = balance;
    }
}
