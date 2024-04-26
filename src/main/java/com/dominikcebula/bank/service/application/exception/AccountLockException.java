package com.dominikcebula.bank.service.application.exception;

import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.exception.ReportableException;

public class AccountLockException extends ReportableException {
    public AccountLockException(AccountId accountId) {
        super(String.format("Unable to lock account [%s]. Please try again later.", accountId.getAccountNumber()));
    }
}
