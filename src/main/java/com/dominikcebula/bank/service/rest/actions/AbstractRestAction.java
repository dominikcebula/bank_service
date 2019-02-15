package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.logging.Loggers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

abstract class AbstractRestAction<I, R> implements Route {

    private Logger logger = LoggerFactory.getLogger(Loggers.REST);

    private final Gson gson = new GsonBuilder().create();

    private final Class<I> requestClass;
    private final Class<R> responseClass;

    AbstractRestAction(Class<I> requestClass, Class<R> responseClass) {
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        logger.info(String.format("Handing request %s %s",
                request.requestMethod(), request.uri()
        ));

        logger.info("Reading JSON Data");
        I requestObject = gson.fromJson(request.body(), requestClass);

        logger.info("Post processing JSON Data");
        postProcessRequest(requestObject);

        logger.info("Executing handler");
        R responseObject = handleRequest(requestObject);

        logger.info("Creating response");
        return gson.toJson(responseObject, responseClass);
    }

    void postProcessRequest(I requestObject) throws Exception {
    }

    abstract R handleRequest(I request) throws Exception;
}
