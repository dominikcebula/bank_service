package com.dominikcebula.bank.service;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacade;
import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.configuration.Configuration;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Configuration.class).in(Singleton.class);
        bind(BankActionsFacade.class).in(Singleton.class);
        bind(AccountDao.class).in(Singleton.class);
    }
}
