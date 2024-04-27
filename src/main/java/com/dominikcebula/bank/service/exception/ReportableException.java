package com.dominikcebula.bank.service.exception;

public class ReportableException extends RuntimeException {
    public ReportableException(String message) {
        super(message);
    }

    public ReportableException(String message, Throwable cause) {
        super(message, cause);
    }
}
