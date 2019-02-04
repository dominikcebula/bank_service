package com.dominikcebula.bank.service.rest.filters;

import spark.Filter;
import spark.Request;
import spark.Response;

public class ResponseFilter implements Filter {

    @Override
    public void handle(Request request, Response response) {
        response.type("application/json; charset=utf-8");
    }
}
