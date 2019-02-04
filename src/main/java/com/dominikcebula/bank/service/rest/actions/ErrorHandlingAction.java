package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;
import com.dominikcebula.bank.service.rest.json.GsonFactory;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

public class ErrorHandlingAction implements Route {

    private Gson gson = new GsonFactory().create();

    @Override
    public Object handle(Request request, Response response) {
        return gson.toJson(
                new ErrorResponse("Error occurred while processing request.")
        );
    }
}
