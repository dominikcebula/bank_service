package com.dominikcebula.bank.service.application.exception;

import com.dominikcebula.bank.service.exception.ReportableException;

public class AccountMissingException extends ReportableException {
    public AccountMissingException(String message) {
        super(message);
    }
}
