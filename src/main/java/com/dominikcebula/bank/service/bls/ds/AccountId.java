package com.dominikcebula.bank.service.bls.ds;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;

@Getter
@EqualsAndHashCode
public class AccountId {
    public static final int ACCOUNT_ID_LENGTH = 16;

    private final String accountNumber;

    private AccountId(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public static AccountId createAccountNumber(String accountNumber) {
        return new AccountId(accountNumber);
    }

    public static AccountId createRandomAccountId() {
        return new AccountId(RandomStringUtils.randomNumeric(ACCOUNT_ID_LENGTH));
    }

    @Override
    public String toString() {
        return accountNumber;
    }
}
