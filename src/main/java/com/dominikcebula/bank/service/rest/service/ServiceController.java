package com.dominikcebula.bank.service.rest.service;

import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.rest.server.RestServer;
import com.google.inject.Injector;

public class ServiceController {
    private final Injector injector;

    private final AwaitServiceStopped awaitServiceStopped = new AwaitServiceStopped();

    private RestServer restServer;
    private final Configuration configuration;

    public ServiceController(Injector injector) {
        this.injector = injector;
        this.configuration = injector.getInstance(Configuration.class);
    }

    public void start() {
        restServer = injector.getInstance(RestServer.class);
        restServer.start();
    }

    public void stop() {
        if (restServer != null) {
            restServer.stop();

            // https://github.com/perwendel/spark/issues/705
            awaitServiceStopped.await(configuration);

            restServer = null;
        }
    }
}
