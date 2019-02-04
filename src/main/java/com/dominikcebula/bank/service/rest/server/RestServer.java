package com.dominikcebula.bank.service.rest.server;

import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.rest.actions.ErrorHandlingAction;
import com.dominikcebula.bank.service.rest.actions.IndexAction;
import com.dominikcebula.bank.service.rest.actions.ListAccountsAction;
import com.dominikcebula.bank.service.rest.actions.OpenAccountAction;
import com.dominikcebula.bank.service.rest.filters.ResponseFilter;
import com.dominikcebula.bank.service.rest.validator.ValidatorException;
import com.dominikcebula.bank.service.rest.validator.ValidatorExceptionHandler;
import spark.Spark;

public class RestServer {

    private final Configuration configuration = Configuration.getInstance();

    public void start() {
        Spark.port(configuration.getPort());
        Spark.threadPool(configuration.getMaxThreads());

        Spark.after(new ResponseFilter());

        Spark.notFound(new ErrorHandlingAction());
        Spark.internalServerError(new ErrorHandlingAction());
        Spark.exception(ValidatorException.class, new ValidatorExceptionHandler());

        Spark.get("/", new IndexAction());
        Spark.post("/accounts/open", new OpenAccountAction());
        Spark.get("/accounts/list", new ListAccountsAction());
    }
}
