package com.dominikcebula.bank.service.bls.utils;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;

import java.util.Set;

public class AccountIdGenerator {
    private static final int MAX_NUMBER_OF_TRIES = 100;

    public AccountId generateAccountId(Set<AccountId> alreadyGeneratedAccounts) throws AccountOpenException {
        int tryNo = 0;

        while (tryNo++ < MAX_NUMBER_OF_TRIES) {
            AccountId accountId = AccountId.createRandomAccountId();

            if (!alreadyGeneratedAccounts.contains(accountId))
                return accountId;
        }

        throw new AccountOpenException("Unable to generate account id");
    }
}
