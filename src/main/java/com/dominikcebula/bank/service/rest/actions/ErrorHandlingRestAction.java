package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ApiErrorResponse;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.google.inject.Inject;

public class ErrorHandlingRestAction extends AbstractRestAction<Void, ApiErrorResponse> {

    static final String ERROR_MESSAGE = "Error occurred while processing request.";

    @Inject
    public ErrorHandlingRestAction(GsonProvider gsonProvider) {
        super(gsonProvider, Void.class, ApiErrorResponse.class);
    }

    @Override
    ApiErrorResponse handleRequest(Void request) {
        return new ApiErrorResponse().code(ApiCode.FAILED).message(ERROR_MESSAGE);
    }
}
