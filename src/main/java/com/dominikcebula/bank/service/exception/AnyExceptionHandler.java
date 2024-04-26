package com.dominikcebula.bank.service.exception;

import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ApiErrorResponse;
import com.dominikcebula.bank.service.dto.ModelApiResponse;
import com.dominikcebula.bank.service.logging.Loggers;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class AnyExceptionHandler implements ExceptionHandler<Exception> {
    private final Logger logger = LoggerFactory.getLogger(Loggers.REST);

    private final Gson gson;

    @Inject
    @SuppressWarnings("unused")
    public AnyExceptionHandler(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void handle(Exception exception, Request request, Response response) {
        logger.error(exception.getMessage(), exception);

        response.status(500);

        response.body(
                gson.toJson(
                        new ApiErrorResponse()
                                .status(new ModelApiResponse().code(ApiCode.FAILED))
                                .message("Error occurred while processing request.")
                )
        );
    }
}
