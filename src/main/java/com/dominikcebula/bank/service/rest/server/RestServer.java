package com.dominikcebula.bank.service.rest.server;

import com.dominikcebula.bank.service.Context;
import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.exception.ReportableException;
import com.dominikcebula.bank.service.exception.ReportableExceptionHandler;
import com.dominikcebula.bank.service.rest.actions.*;
import com.dominikcebula.bank.service.rest.filters.ResponseFilter;
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

    public RestServer(Context context) {
        this.configuration = context.getConfiguration();
        this.responseFilter = context.getResponseFilter();
        this.errorHandlingRestAction = context.getErrorHandlingRestAction();
        this.reportableExceptionHandler = context.getReportableExceptionHandler();
        this.indexRestAction = context.getIndexRestAction();
        this.openAccountRestAction = context.getOpenAccountRestAction();
        this.transferMoneyAction = context.getTransferMoneyRestAction();
        this.listAccountsRestAction = context.getListAccountsRestAction();
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
}
