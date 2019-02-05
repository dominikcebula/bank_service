package com.dominikcebula.bank.service;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacade;
import com.dominikcebula.bank.service.bls.actions.ListAccountsAction;
import com.dominikcebula.bank.service.bls.actions.OpenAccountAction;
import com.dominikcebula.bank.service.bls.actions.TransferMoneyAction;
import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.bls.utils.AccountIdGenerator;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.exception.ReportableExceptionHandler;
import com.dominikcebula.bank.service.rest.actions.*;
import com.dominikcebula.bank.service.rest.filters.ResponseFilter;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.dominikcebula.bank.service.rest.json.deserializer.AccountIdDeserializer;
import com.dominikcebula.bank.service.rest.json.deserializer.MoneyDeserializer;
import com.dominikcebula.bank.service.rest.json.serializer.AccountIdSerializer;
import com.dominikcebula.bank.service.rest.json.serializer.MoneySerializer;
import com.google.gson.Gson;
import lombok.Getter;

@Getter
public class Context {
    private final Configuration configuration = new Configuration();
    private final AccountDao accountDao = new AccountDao();

    private final AccountIdGenerator accountIdGenerator = new AccountIdGenerator();
    private final MoneyFactory moneyFactory = new MoneyFactory(configuration);

    private final OpenAccountAction openAccountAction = new OpenAccountAction(accountDao, accountIdGenerator);
    private final TransferMoneyAction transferMoneyAction = new TransferMoneyAction(accountDao);
    private final ListAccountsAction listAccountsAction = new ListAccountsAction(accountDao, moneyFactory);
    private final BankActionsFacade bankActionsFacade = new BankActionsFacade(openAccountAction, transferMoneyAction, listAccountsAction);

    private final MoneySerializer moneySerializer = new MoneySerializer();
    private final MoneyDeserializer moneyDeserializer = new MoneyDeserializer(moneyFactory);
    private final AccountIdSerializer accountIdSerializer = new AccountIdSerializer();
    private final AccountIdDeserializer accountIdDeserializer = new AccountIdDeserializer();
    private final Gson gson = new GsonProvider(moneySerializer, moneyDeserializer, accountIdSerializer, accountIdDeserializer).provide();

    private final ResponseFilter responseFilter = new ResponseFilter();
    private final ErrorHandlingRestAction errorHandlingRestAction = new ErrorHandlingRestAction(gson);
    private final ReportableExceptionHandler reportableExceptionHandler = new ReportableExceptionHandler(gson);

    private final IndexRestAction indexRestAction = new IndexRestAction(gson);
    private final OpenAccountRestAction openAccountRestAction = new OpenAccountRestAction(gson, bankActionsFacade);
    private final TransferMoneyRestAction transferMoneyRestAction = new TransferMoneyRestAction(gson, bankActionsFacade);
    private final ListAccountsRestAction listAccountsRestAction = new ListAccountsRestAction(gson, bankActionsFacade);
}
