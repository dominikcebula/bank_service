package com.dominikcebula.bank.service;

import com.dominikcebula.bank.service.rest.server.RestServer;

class Runner {

    public static void main(String... args) {
        new RestServer().start();
    }
}