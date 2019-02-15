package com.dominikcebula.bank.service.spark;

import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.guice.ContextAwareTest;
import com.dominikcebula.bank.service.rest.service.ServiceController;
import com.dominikcebula.bank.service.utils.RestClient;
import com.google.gson.GsonBuilder;
import org.junit.After;
import org.junit.Before;

public abstract class SparkRestServerAwareTest extends ContextAwareTest {

    private ServiceController serviceController;

    @Before
    public void setUp() {
        super.setUp();
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
                new GsonBuilder().create()
        );
    }
}
