package com.dominikcebula.bank.service.rest.actions.base;

import com.dominikcebula.bank.service.ServiceModule;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;

public class NoActionsFacadeInContextIntegrationTest extends SparkRestServerAwareTest {
    @Override
    protected ServiceModule getServiceModule() {
        return new NoActionsFacadeServiceModule();
    }

    private static class NoActionsFacadeServiceModule extends ServiceModule {
        @Override
        protected void configureBankActionsFacade() {
        }
    }
}
