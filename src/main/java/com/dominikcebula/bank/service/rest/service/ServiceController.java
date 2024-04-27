package com.dominikcebula.bank.service.rest.service;

import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.logging.Loggers;
import com.dominikcebula.bank.service.rest.server.RestServer;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceController {

    private final Logger logger = LoggerFactory.getLogger(Loggers.SERVER);

    private final Injector injector;
    private final Configuration configuration;

    private RestServer restServer;

    public ServiceController(Injector injector) {
        this.injector = injector;
        this.configuration = injector.getInstance(Configuration.class);
    }

    public void start() {
        logger.info("Starting service");

        restServer = injector.getInstance(RestServer.class);
        restServer.start();
    }

    public void stop() {
        if (restServer != null) {
            logger.info("Stopping service");
            restServer.stop();

            restServer = null;
        }
    }
}
