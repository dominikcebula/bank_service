package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;

public class ErrorHandlingAction extends AbstractRestAction<Void, ErrorResponse> {

    public ErrorHandlingAction() {
        super(Void.class, ErrorResponse.class);
    }

    @Override
    ErrorResponse handleRequest(Void request) {
        return new ErrorResponse("Error occurred while processing request.");
    }
}
