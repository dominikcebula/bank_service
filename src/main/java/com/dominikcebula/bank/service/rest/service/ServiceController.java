package com.dominikcebula.bank.service.rest.service;

import com.dominikcebula.bank.service.ServiceModule;
import com.dominikcebula.bank.service.rest.server.RestServer;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ServiceController {
    private final ServiceModule serviceModule;
    private RestServer restServer;

    public ServiceController(ServiceModule serviceModule) {
        this.serviceModule = serviceModule;
    }

    public void start() {
        Injector injector = Guice.createInjector(serviceModule);
        restServer = injector.getInstance(RestServer.class);
        restServer.start();
    }

    public void stop() {
        if (restServer != null)
            restServer.stop();
    }
}
