package com.dominikcebula.bank.service.rest.ds.response;

import com.dominikcebula.bank.service.bls.ds.AccountId;

public class AccountOpenResponse extends Response {
    public AccountOpenResponse(AccountId accountId) {
        super(Status.SUCCESS, String.format("Opened account: [%s]", accountId));
    }
}
