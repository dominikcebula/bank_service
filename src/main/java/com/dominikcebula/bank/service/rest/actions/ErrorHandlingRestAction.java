package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;
import com.google.gson.Gson;

public class ErrorHandlingRestAction extends AbstractRestAction<Void, ErrorResponse> {

    public ErrorHandlingRestAction(Gson gson) {
        super(gson, Void.class, ErrorResponse.class);
    }

    @Override
    ErrorResponse handleRequest(Void request) {
        return new ErrorResponse("Error occurred while processing request.");
    }
}
