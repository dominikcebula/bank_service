package com.dominikcebula.bank.service.rest.server;

import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.exception.ReportableException;
import com.dominikcebula.bank.service.exception.ReportableExceptionHandler;
import com.dominikcebula.bank.service.rest.actions.*;
import com.dominikcebula.bank.service.rest.filters.ResponseFilter;
import com.google.inject.Inject;
import spark.Spark;

public class RestServer {

    private final Configuration configuration;
    private final ResponseFilter responseFilter;
    private final ErrorHandlingRestAction errorHandlingRestAction;
    private final ReportableExceptionHandler reportableExceptionHandler;
    private final IndexRestAction indexRestAction;
    private final OpenAccountRestAction openAccountRestAction;
    private final TransferMoneyRestAction transferMoneyAction;
    private final ListAccountsRestAction listAccountsRestAction;

    @Inject
    public RestServer(Configuration configuration, ResponseFilter responseFilter,
                      ErrorHandlingRestAction errorHandlingRestAction, ReportableExceptionHandler reportableExceptionHandler,
                      IndexRestAction indexRestAction, OpenAccountRestAction openAccountRestAction,
                      TransferMoneyRestAction transferMoneyAction, ListAccountsRestAction listAccountsRestAction) {
        this.configuration = configuration;
        this.responseFilter = responseFilter;
        this.errorHandlingRestAction = errorHandlingRestAction;
        this.reportableExceptionHandler = reportableExceptionHandler;
        this.indexRestAction = indexRestAction;
        this.openAccountRestAction = openAccountRestAction;
        this.transferMoneyAction = transferMoneyAction;
        this.listAccountsRestAction = listAccountsRestAction;
    }


    public void start() {
        Spark.port(configuration.getPort());
        Spark.threadPool(configuration.getMaxThreads());

        Spark.before(responseFilter);

        Spark.notFound(errorHandlingRestAction);
        Spark.internalServerError(errorHandlingRestAction);
        Spark.exception(ReportableException.class, reportableExceptionHandler);

        Spark.get("/", indexRestAction);
        Spark.post("/accounts/open", openAccountRestAction);
        Spark.post("/transfer", transferMoneyAction);
        Spark.get("/accounts/list", listAccountsRestAction);
    }

    public void stop() {
        Spark.stop();
    }
}
