package com.dominikcebula.bank.service.spark;

import com.dominikcebula.bank.service.ServiceModule;
import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.guice.ComposedServiceModule;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.dominikcebula.bank.service.rest.service.ServiceController;
import com.dominikcebula.bank.service.utils.RestClient;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.junit.After;
import org.junit.Before;

public abstract class SparkRestServerAwareTest {

    private ServiceController serviceController;
    protected Injector injector;

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

    protected RestClient resetClient() {
        return new RestClient(
                injector.getInstance(Configuration.class),
                injector.getInstance(GsonProvider.class).provide()
        );
    }

    private Module getServiceModule() {
        return new ComposedServiceModule(
                new ServiceModule(),
                BoundFieldModule.of(this)
        );
    }
}
