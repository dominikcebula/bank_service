package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.google.inject.Inject;

public class ErrorHandlingRestAction extends AbstractRestAction<Void, ErrorResponse> {

    static final String ERROR_MESSAGE = "Error occurred while processing request.";

    @Inject
    public ErrorHandlingRestAction(GsonProvider gsonProvider) {
        super(gsonProvider, Void.class, ErrorResponse.class);
    }

    @Override
    ErrorResponse handleRequest(Void request) {
        return new ErrorResponse(ERROR_MESSAGE);
    }
}
