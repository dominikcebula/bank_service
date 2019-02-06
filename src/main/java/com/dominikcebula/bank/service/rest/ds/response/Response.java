package com.dominikcebula.bank.service.rest.ds.response;

import lombok.Getter;

@Getter
public class Response {
    private final Status status;
    private final String message;

    Response(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public enum Status {
        SUCCESS,
        ERROR
    }
}
