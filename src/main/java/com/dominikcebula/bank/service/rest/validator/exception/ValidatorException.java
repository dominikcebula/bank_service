package com.dominikcebula.bank.service.rest.validator.exception;

import com.dominikcebula.bank.service.exception.ReportableException;

public class ValidatorException extends ReportableException {
    public ValidatorException(String message) {
        super(message);
    }
}
