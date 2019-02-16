package com.dominikcebula.bank.service.bls.exception;

import com.dominikcebula.bank.service.exception.ReportableException;

public class AccountCreateException extends ReportableException {
    public AccountCreateException(String message) {
        super(message);
    }
}
