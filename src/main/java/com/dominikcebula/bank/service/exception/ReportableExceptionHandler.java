package com.dominikcebula.bank.service.exception;

import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;
import com.google.gson.Gson;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class ReportableExceptionHandler implements ExceptionHandler<ReportableException> {

    private final Gson gson;

    public ReportableExceptionHandler(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void handle(ReportableException exception, Request request, Response response) {
        response.body(
                gson.toJson(
                        new ErrorResponse(exception.getMessage())
                )
        );
    }
}
