package com.dominikcebula.bank.service;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacade;
import com.dominikcebula.bank.service.bls.actions.ListAccountsAction;
import com.dominikcebula.bank.service.bls.actions.OpenAccountAction;
import com.dominikcebula.bank.service.bls.actions.TransferMoneyAction;
import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.bls.utils.AccountIdGenerator;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.rest.actions.*;
import com.dominikcebula.bank.service.rest.filters.ResponseFilter;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.dominikcebula.bank.service.rest.json.deserializer.MoneyDeserializer;
import com.dominikcebula.bank.service.rest.json.serializer.AccountIdSerializer;
import com.dominikcebula.bank.service.rest.json.serializer.MoneySerializer;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorExceptionHandler;
import com.google.gson.Gson;
import lombok.Getter;

@Getter
public class Context {
    private Configuration configuration = new Configuration();
    private AccountDao accountDao = new AccountDao();

    private AccountIdGenerator accountIdGenerator = new AccountIdGenerator();
    private MoneyFactory moneyFactory = new MoneyFactory(configuration);

    private OpenAccountAction openAccountAction = new OpenAccountAction(accountDao, accountIdGenerator);
    private TransferMoneyAction transferMoneyAction = new TransferMoneyAction(accountDao);
    private ListAccountsAction listAccountsAction = new ListAccountsAction(accountDao, moneyFactory);
    private BankActionsFacade bankActionsFacade = new BankActionsFacade(openAccountAction, transferMoneyAction, listAccountsAction);

    private MoneySerializer moneySerializer = new MoneySerializer();
    private MoneyDeserializer moneyDeserializer = new MoneyDeserializer(moneyFactory);
    private AccountIdSerializer accountIdSerializer = new AccountIdSerializer();
    private Gson gson = new GsonProvider(moneySerializer, moneyDeserializer, accountIdSerializer).provide();

    private ResponseFilter responseFilter = new ResponseFilter();
    private ErrorHandlingRestAction errorHandlingRestAction = new ErrorHandlingRestAction(gson);
    private ValidatorExceptionHandler validatorExceptionHandler = new ValidatorExceptionHandler(gson);

    private IndexRestAction indexRestAction = new IndexRestAction(gson);
    private OpenAccountRestAction openAccountRestAction = new OpenAccountRestAction(gson, bankActionsFacade);
    private TransferMoneyRestAction transferMoneyRestAction = new TransferMoneyRestAction(gson, bankActionsFacade);
    private ListAccountsRestAction listAccountsRestAction = new ListAccountsRestAction(gson, bankActionsFacade);
}
