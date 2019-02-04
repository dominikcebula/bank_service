package com.dominikcebula.bank.service.rest.validator;

import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;
import com.dominikcebula.bank.service.rest.json.GsonFactory;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class ValidatorExceptionHandler implements ExceptionHandler<ValidatorException> {
    @Override
    public void handle(ValidatorException exception, Request request, Response response) {
        response.body(
                new GsonFactory().create().toJson(
                        new ErrorResponse(exception.getMessage())
                )
        );
    }
}
