package com.dominikcebula.bank.service.configuration;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static com.dominikcebula.bank.service.configuration.Configuration.*;
import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ConfigurationTest {

    private static final String SERVICE_HOST_VALUE = "my.server.com";
    private static final int SERVICE_PORT_VALUE = 8081;
    private static final int SERVICE_MAX_THREADS_VALUES = 500;

    private Properties properties;
    private Configuration configuration;

    @BeforeEach
    void setUp() {
        properties = new Properties();
        configuration = new Configuration(properties);
    }

    @Test
    void shouldFetchHostFromProperty() {
        properties.setProperty(SERVICE_HOST, SERVICE_HOST_VALUE);

        assertEquals(SERVICE_HOST_VALUE, configuration.getHost());
    }

    @Test
    void shouldFetchPortFromProperty() {
        properties.setProperty(SERVICE_PORT, valueOf(SERVICE_PORT_VALUE));

        assertEquals(SERVICE_PORT_VALUE, configuration.getPort());
    }

    @Test
    void shouldFetchMaxThreadsFromProperty() {
        properties.setProperty(SERVICE_MAX_THREADS, valueOf(SERVICE_MAX_THREADS_VALUES));

        assertEquals(SERVICE_MAX_THREADS_VALUES, configuration.getMaxThreads());
    }
}