package com.dominikcebula.bank.service;

import com.dominikcebula.bank.service.rest.server.RestServer;
import com.google.inject.Guice;
import com.google.inject.Injector;

class Runner {

    public static void main(String... args) {

        Injector injector = Guice.createInjector(new ServiceModule());

        RestServer restServer = injector.getInstance(RestServer.class);

        restServer.start();
    }
}