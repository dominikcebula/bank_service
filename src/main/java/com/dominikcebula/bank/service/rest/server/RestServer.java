package com.dominikcebula.bank.service.rest.server;

import com.dominikcebula.bank.service.Context;
import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.rest.actions.ErrorHandlingRestAction;
import com.dominikcebula.bank.service.rest.actions.IndexRestAction;
import com.dominikcebula.bank.service.rest.actions.ListAccountsRestAction;
import com.dominikcebula.bank.service.rest.actions.OpenAccountRestAction;
import com.dominikcebula.bank.service.rest.filters.ResponseFilter;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorExceptionHandler;
import spark.Spark;

public class RestServer {

    private final Configuration configuration;
    private final ResponseFilter responseFilter;
    private final ErrorHandlingRestAction errorHandlingRestAction;
    private final ValidatorExceptionHandler validatorExceptionHandler;
    private final IndexRestAction indexRestAction;
    private final OpenAccountRestAction openAccountRestAction;
    private final ListAccountsRestAction listAccountsRestAction;

    public RestServer(Context context) {
        this.configuration = context.getConfiguration();
        this.responseFilter = context.getResponseFilter();
        this.errorHandlingRestAction = context.getErrorHandlingRestAction();
        this.validatorExceptionHandler = context.getValidatorExceptionHandler();
        this.indexRestAction = context.getIndexRestAction();
        this.openAccountRestAction = context.getOpenAccountRestAction();
        this.listAccountsRestAction = context.getListAccountsRestAction();
    }

    public void start() {
        Spark.port(configuration.getPort());
        Spark.threadPool(configuration.getMaxThreads());

        Spark.after(responseFilter);

        Spark.notFound(errorHandlingRestAction);
        Spark.internalServerError(errorHandlingRestAction);
        Spark.exception(ValidatorException.class, validatorExceptionHandler);

        Spark.get("/", indexRestAction);
        Spark.post("/accounts/open", openAccountRestAction);
        Spark.get("/accounts/list", listAccountsRestAction);
    }
}
