package com.dominikcebula.bank.service.bls.exception;

import com.dominikcebula.bank.service.exception.ReportableException;

public class WithdrawException extends ReportableException {
    public WithdrawException(String message) {
        super(message);
    }
}
