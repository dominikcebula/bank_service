package com.dominikcebula.bank.service.exception;

import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ApiErrorResponse;
import com.dominikcebula.bank.service.dto.ModelApiResponse;
import com.dominikcebula.bank.service.logging.Loggers;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class ReportableExceptionHandler implements ExceptionHandler<ReportableException> {

    private final Logger logger = LogManager.getLogger(Loggers.REST);

    private final Gson gson;

    @Inject
    @SuppressWarnings("unused")
    public ReportableExceptionHandler(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void handle(ReportableException exception, Request request, Response response) {
        logger.error(exception.getMessage(), exception);

        response.status(400);

        response.body(
                gson.toJson(
                        new ApiErrorResponse()
                                .status(new ModelApiResponse().code(ApiCode.FAILED))
                                .message(exception.getMessage())
                )
        );
    }
}
