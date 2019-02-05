package com.dominikcebula.bank.service.rest.validator.exception;

import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;
import com.dominikcebula.bank.service.rest.json.GsonFactory;
import com.google.gson.Gson;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class ValidatorExceptionHandler implements ExceptionHandler<ValidatorException> {

    private final Gson gson = new GsonFactory().create();

    @Override
    public void handle(ValidatorException exception, Request request, Response response) {
        response.body(
                gson.toJson(
                        new ErrorResponse(exception.getMessage())
                )
        );
    }
}
