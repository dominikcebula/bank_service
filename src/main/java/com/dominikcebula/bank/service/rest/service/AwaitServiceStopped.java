package com.dominikcebula.bank.service.rest.service;

import com.dominikcebula.bank.service.configuration.Configuration;

import java.io.IOException;
import java.net.Socket;

class AwaitServiceStopped {

    void await(Configuration configuration) {
        try {
            int maxNumberOfWaits = 200;

            while (!wasSockedClosed(configuration.getHost(), configuration.getPort()) && --maxNumberOfWaits > 0) {
                Thread.sleep(10);
            }

            if (maxNumberOfWaits == 0)
                throw new AwaitServiceStoppedException("Reached Timeout of waiting for service to stop");
        } catch (InterruptedException e) {
            throw new AwaitServiceStoppedException("Error occurred while waiting for service to stop: " + e.getMessage(), e);
        }
    }

    private boolean wasSockedClosed(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    private static class AwaitServiceStoppedException extends RuntimeException {
        private AwaitServiceStoppedException(String message) {
            super(message);
        }

        private AwaitServiceStoppedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
