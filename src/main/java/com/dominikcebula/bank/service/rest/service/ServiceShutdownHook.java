package com.dominikcebula.bank.service.rest.service;

public class ServiceShutdownHook extends Thread {

    private final ServiceController serviceController;

    public ServiceShutdownHook(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    @Override
    public void run() {
        serviceController.stop();
    }
}
