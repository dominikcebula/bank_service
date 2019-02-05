package com.dominikcebula.bank.service;

import com.dominikcebula.bank.service.rest.service.ServiceController;
import com.dominikcebula.bank.service.rest.service.ServiceShutdownHook;

class Runner {

    public static void main(String... args) {

        ServiceController serviceController = new ServiceController(new ServiceModule());
        serviceController.start();

        Runtime.getRuntime().addShutdownHook(new ServiceShutdownHook(serviceController));
    }
}