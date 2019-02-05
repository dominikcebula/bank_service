package com.dominikcebula.bank.service;

import com.dominikcebula.bank.service.bls.actions.ActionsFacade;
import com.dominikcebula.bank.service.bls.actions.ListAccountsAction;
import com.dominikcebula.bank.service.bls.actions.OpenAccountAction;
import com.dominikcebula.bank.service.bls.actions.TransferMoneyAction;
import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.bls.utils.AccountIdGenerator;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.rest.server.RestServer;

class Runner {

    public static void main(String... args) {

        Configuration configuration = new Configuration();
        MoneyFactory moneyFactory = new MoneyFactory(configuration);
        AccountIdGenerator accountIdGenerator = new AccountIdGenerator();
        AccountDao accountDao = new AccountDao();

        OpenAccountAction openAccountAction = new OpenAccountAction(accountDao, accountIdGenerator);
        TransferMoneyAction transferMoneyAction = new TransferMoneyAction(accountDao);
        ListAccountsAction listAccountsAction = new ListAccountsAction(accountDao, moneyFactory);

        ActionsFacade actionsFacade = new ActionsFacade(openAccountAction, transferMoneyAction, listAccountsAction);

        new RestServer(
                actionsFacade,
                configuration,
                moneyFactory
        ).start();
    }
}