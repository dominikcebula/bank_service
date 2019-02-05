package com.dominikcebula.bank.service.bls.exception;

import com.dominikcebula.bank.service.exception.ReportableException;

public class AccountOpenException extends ReportableException {
    public AccountOpenException(String message) {
        super(message);
    }
}
