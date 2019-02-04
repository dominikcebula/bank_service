package com.dominikcebula.bank.service.rest.ds.response;

import lombok.Getter;

@Getter
public class ErrorResponse extends Response {

    public ErrorResponse(String message) {
        super(Status.ERROR, message);
    }
}
