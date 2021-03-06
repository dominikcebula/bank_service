package com.dominikcebula.bank.service.rest.server;

import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.exception.ReportableException;
import com.dominikcebula.bank.service.exception.ReportableExceptionHandler;
import com.dominikcebula.bank.service.rest.actions.*;
import com.dominikcebula.bank.service.rest.filters.ResponseFilter;
import com.google.inject.Inject;
import spark.Spark;

import static com.dominikcebula.bank.service.rest.actions.IndexRestAction.INDEX_ACTION_URI;
import static com.dominikcebula.bank.service.rest.actions.ListAccountsRestAction.ACCOUNT_LIST_URI;
import static com.dominikcebula.bank.service.rest.actions.CreateAccountRestAction.ACCOUNTS_CREATE_URI;
import static com.dominikcebula.bank.service.rest.actions.TransferMoneyRestAction.TRANSFER_URI;

public class RestServer {

    private final Configuration configuration;
    private final ResponseFilter responseFilter;
    private final ErrorHandlingRestAction errorHandlingRestAction;
    private final ReportableExceptionHandler reportableExceptionHandler;
    private final IndexRestAction indexRestAction;
    private final CreateAccountRestAction createAccountRestAction;
    private final TransferMoneyRestAction transferMoneyAction;
    private final ListAccountsRestAction listAccountsRestAction;

    @Inject
    public RestServer(Configuration configuration, ResponseFilter responseFilter,
                      ErrorHandlingRestAction errorHandlingRestAction, ReportableExceptionHandler reportableExceptionHandler,
                      IndexRestAction indexRestAction, CreateAccountRestAction createAccountRestAction,
                      TransferMoneyRestAction transferMoneyAction, ListAccountsRestAction listAccountsRestAction) {
        this.configuration = configuration;
        this.responseFilter = responseFilter;
        this.errorHandlingRestAction = errorHandlingRestAction;
        this.reportableExceptionHandler = reportableExceptionHandler;
        this.indexRestAction = indexRestAction;
        this.createAccountRestAction = createAccountRestAction;
        this.transferMoneyAction = transferMoneyAction;
        this.listAccountsRestAction = listAccountsRestAction;
    }


    public void start() {
        Spark.ipAddress(configuration.getHost());
        Spark.port(configuration.getPort());
        Spark.threadPool(configuration.getMaxThreads());

        Spark.before(responseFilter);

        Spark.notFound(errorHandlingRestAction);
        Spark.internalServerError(errorHandlingRestAction);
        Spark.exception(ReportableException.class, reportableExceptionHandler);

        Spark.get(INDEX_ACTION_URI, indexRestAction);
        Spark.post(ACCOUNTS_CREATE_URI, createAccountRestAction);
        Spark.post(TRANSFER_URI, transferMoneyAction);
        Spark.get(ACCOUNT_LIST_URI, listAccountsRestAction);
    }

    public void stop() {
        Spark.stop();
    }
}
