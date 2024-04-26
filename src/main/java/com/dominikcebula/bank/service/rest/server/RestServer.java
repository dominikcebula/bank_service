package com.dominikcebula.bank.service.rest.server;

import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.exception.AnyExceptionHandler;
import com.dominikcebula.bank.service.exception.ReportableException;
import com.dominikcebula.bank.service.exception.ReportableExceptionHandler;
import com.dominikcebula.bank.service.rest.actions.*;
import com.dominikcebula.bank.service.rest.filters.ResponseFilter;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import spark.Spark;

import static com.dominikcebula.bank.service.rest.actions.CreateAccountRestAction.ACCOUNTS_CREATE_URI;
import static com.dominikcebula.bank.service.rest.actions.IndexRestAction.INDEX_ACTION_URI;
import static com.dominikcebula.bank.service.rest.actions.ListAccountsRestAction.ACCOUNT_LIST_URI;
import static com.dominikcebula.bank.service.rest.actions.TransferMoneyRestAction.TRANSFER_URI;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class RestServer {

    private final Configuration configuration;
    private final ResponseFilter responseFilter;
    private final ErrorHandlingRestAction errorHandlingRestAction;
    private final ReportableExceptionHandler reportableExceptionHandler;
    private final AnyExceptionHandler anyExceptionHandler;
    private final IndexRestAction indexRestAction;
    private final CreateAccountRestAction createAccountRestAction;
    private final TransferMoneyRestAction transferMoneyAction;
    private final ListAccountsRestAction listAccountsRestAction;

    public void start() {
        Spark.ipAddress(configuration.getHost());
        Spark.port(configuration.getPort());
        Spark.threadPool(configuration.getMaxThreads());

        Spark.before(responseFilter);

        Spark.notFound(errorHandlingRestAction);
        Spark.internalServerError(errorHandlingRestAction);
        Spark.exception(ReportableException.class, reportableExceptionHandler);
        Spark.exception(Exception.class, anyExceptionHandler);

        Spark.get(INDEX_ACTION_URI, indexRestAction);
        Spark.post(ACCOUNTS_CREATE_URI, createAccountRestAction);
        Spark.post(TRANSFER_URI, transferMoneyAction);
        Spark.get(ACCOUNT_LIST_URI, listAccountsRestAction);

        Spark.awaitInitialization();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
