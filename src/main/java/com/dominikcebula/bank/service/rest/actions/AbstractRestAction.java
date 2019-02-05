package com.dominikcebula.bank.service.rest.actions;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

abstract class AbstractRestAction<I, R> implements Route {

    private final Gson gson;

    private final Class<I> requestClass;
    private final Class<R> responseClass;

    AbstractRestAction(Gson gson, Class<I> requestClass, Class<R> responseClass) {
        this.gson = gson;
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        I requestObject = gson.fromJson(request.body(), requestClass);
        postProcessRequest(requestObject);

        R responseObject = handleRequest(requestObject);

        return gson.toJson(responseObject, responseClass);
    }

    void postProcessRequest(I requestObject) throws Exception {
    }

    abstract R handleRequest(I request) throws Exception;
}
