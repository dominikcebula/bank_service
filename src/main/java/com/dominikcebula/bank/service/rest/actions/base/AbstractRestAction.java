package com.dominikcebula.bank.service.rest.actions.base;

import com.dominikcebula.bank.service.logging.Loggers;
import com.dominikcebula.bank.service.rest.processors.RequestProcessor;
import com.dominikcebula.bank.service.rest.processors.ResponseProcessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collections;
import java.util.List;

public abstract class AbstractRestAction<I, R> implements Route {

    private final Logger logger = LogManager.getLogger(Loggers.REST);

    private final Gson gson = new GsonBuilder().create();

    private final Class<I> requestClass;
    private final Class<R> responseClass;

    protected AbstractRestAction(Class<I> requestClass, Class<R> responseClass) {
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

        logger.info("Post processing Request Object");
        postProcessRequestObject(requestObject);

        logger.info("Executing handler");
        R responseObject = handleRequest(requestObject);

        logger.info("Post processing Response Object");
        postProcessResponseObject(responseObject);

        logger.info("Creating response");
        String json = gson.toJson(responseObject, responseClass);

        response.status(getStatusCode());

        return json;
    }

    private void postProcessRequestObject(I requestObject) {
        getRequestProcessors().forEach(p -> callRequestProcessor(p, requestObject));
    }

    private void postProcessResponseObject(R responseObject) {
        getResponseProcessors().forEach(p -> callResponseProcessor(p, responseObject));
    }

    @SneakyThrows
    private void callRequestProcessor(RequestProcessor<I> requestProcessor, I requestObject) {
        requestProcessor.process(requestObject);
    }

    @SneakyThrows
    private void callResponseProcessor(ResponseProcessor<R> responseProcessor, R responseObject) {
        responseProcessor.process(responseObject);
    }

    List<RequestProcessor<I>> getRequestProcessors() {
        return Collections.emptyList();
    }

    List<ResponseProcessor<R>> getResponseProcessors() {
        return Collections.emptyList();
    }

    protected abstract R handleRequest(I request) throws Exception;

    protected int getStatusCode() {
        return 200;
    }
}
