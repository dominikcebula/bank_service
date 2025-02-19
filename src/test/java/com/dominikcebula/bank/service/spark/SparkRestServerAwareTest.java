package com.dominikcebula.bank.service.spark;

import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.guice.ContextAwareTest;
import com.dominikcebula.bank.service.rest.service.ServiceController;
import com.dominikcebula.bank.service.utils.RestClient;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


public abstract class SparkRestServerAwareTest extends ContextAwareTest {

    private ServiceController serviceController;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        serviceController = new ServiceController(injector);
        serviceController.start();
    }

    @AfterEach
    protected void tearDown() {
        serviceController.stop();
    }

    protected RestClient resetClient() {
        return new RestClient(
                injector.getInstance(Configuration.class),
                new GsonBuilder().create()
        );
    }
}
