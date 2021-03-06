package com.dominikcebula.bank.service;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacade;
import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.rest.server.RestServer;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RestServer.class).in(Singleton.class);
        bind(Configuration.class).in(Singleton.class);
        bind(BankActionsFacade.class).in(Singleton.class);
        bind(AccountDao.class).in(Singleton.class);
    }
}
