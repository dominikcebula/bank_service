package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.ServiceModule;
import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.dominikcebula.bank.service.rest.service.ServiceController;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.After;
import org.junit.Before;

public abstract class SparkRestServerAwareTest {

    private ServiceController serviceController;
    private Injector injector;

    @Before
    public void setUp() {
        injector = Guice.createInjector(getServiceModule());
        serviceController = new ServiceController(injector);
        serviceController.start();
    }

    @After
    public void tearDown() {
        serviceController.stop();
    }

    RestClient resetClient() {
        return new RestClient(
                injector.getInstance(Configuration.class),
                injector.getInstance(GsonProvider.class).provide()
        );
    }

    ServiceModule getServiceModule() {
        return new ServiceModule();
    }
}
