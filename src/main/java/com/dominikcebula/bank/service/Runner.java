package com.dominikcebula.bank.service;

import com.dominikcebula.bank.service.rest.service.ServiceController;
import com.dominikcebula.bank.service.rest.service.ServiceShutdownHook;
import com.google.inject.Guice;
import com.google.inject.Injector;

class Runner {

    public static void main(String... args) {

        Injector injector = Guice.createInjector(new ServiceModule());

        ServiceController serviceController = new ServiceController(injector);
        serviceController.start();

        Runtime.getRuntime().addShutdownHook(new ServiceShutdownHook(serviceController));
    }
}