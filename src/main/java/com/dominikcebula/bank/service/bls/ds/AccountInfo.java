package com.dominikcebula.bank.service.bls.ds;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.javamoney.moneta.Money;

@Getter
@EqualsAndHashCode
public class AccountInfo {
    private final AccountId accountId;
    private final Money balance;

    public AccountInfo(AccountId accountId, Money balance) {
        this.accountId = accountId;
        this.balance = balance;
    }
}
