package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ApiErrorResponse;
import com.dominikcebula.bank.service.dto.ModelApiResponse;
import com.google.inject.Inject;

public class ErrorHandlingRestAction extends AbstractRestAction<Void, ApiErrorResponse> {

    static final String ERROR_MESSAGE = "Error occurred while processing request.";

    @Inject
    public ErrorHandlingRestAction() {
        super(Void.class, ApiErrorResponse.class);
    }

    @Override
    ApiErrorResponse handleRequest(Void request) {
        return new ApiErrorResponse().status(new ModelApiResponse().code(ApiCode.FAILED)).message(ERROR_MESSAGE);
    }
}
