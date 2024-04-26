package com.dominikcebula.bank.service.application.exception;

import com.dominikcebula.bank.service.exception.ReportableException;

public class TransferException extends ReportableException {
    public TransferException(String message, Throwable cause) {
        super(message, cause);
    }
}
