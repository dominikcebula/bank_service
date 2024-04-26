package com.dominikcebula.bank.service.application.utils;

import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.application.exception.AccountCreateException;

import java.util.Set;

public class AccountIdGenerator {
    private static final int MAX_NUMBER_OF_TRIES = 100;
    static final String MESSAGE_GENERATION_ERROR = "Unable to generate account id";

    public AccountId generateAccountId(Set<AccountId> alreadyGeneratedAccounts) throws AccountCreateException {
        int tryNo = 0;

        while (tryNo++ < MAX_NUMBER_OF_TRIES) {
            AccountId accountId = AccountId.createRandomAccountId();

            if (!alreadyGeneratedAccounts.contains(accountId))
                return accountId;
        }

        throw new AccountCreateException(MESSAGE_GENERATION_ERROR);
    }
}
