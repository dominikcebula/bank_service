package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;
import com.dominikcebula.bank.service.rest.json.GsonFactory;
import spark.Request;
import spark.Response;
import spark.Route;

public class ErrorHandlingAction implements Route {
    @Override
    public Object handle(Request request, Response response) {
        return new GsonFactory().create().toJson(
                new ErrorResponse("Error occurred while processing request.")
        );
    }
}
