package com.dominikcebula.bank.service.bls.ds;

import lombok.Getter;
import org.javamoney.moneta.Money;

import java.util.Set;

@Getter
public class AccountsInfo {

    private final Set<AccountInfo> accountsInfo;
    private final Money totalDeposit;

    public AccountsInfo(Set<AccountInfo> accountsInfo, Money totalDeposit) {
        this.accountsInfo = accountsInfo;
        this.totalDeposit = totalDeposit;
    }
}
