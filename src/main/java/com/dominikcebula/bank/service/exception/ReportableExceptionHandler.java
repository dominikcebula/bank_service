package com.dominikcebula.bank.service.exception;

import com.dominikcebula.bank.service.logging.Loggers;
import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class ReportableExceptionHandler implements ExceptionHandler<ReportableException> {

    private Logger logger = LoggerFactory.getLogger(Loggers.REST);

    private final Gson gson;

    @Inject
    public ReportableExceptionHandler(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void handle(ReportableException exception, Request request, Response response) {
        logger.error(exception.getMessage(), exception);

        response.body(
                gson.toJson(
                        new ErrorResponse(exception.getMessage())
                )
        );
    }
}
