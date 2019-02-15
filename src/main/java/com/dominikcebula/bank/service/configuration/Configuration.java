package com.dominikcebula.bank.service.configuration;

import com.google.common.annotations.VisibleForTesting;

import java.util.Properties;
import java.util.function.Function;

public class Configuration {

    static final String SERVICE_HOST = "service.host";
    static final String SERVICE_PORT = "service.port";
    static final String SERVICE_MAX_THREADS = "service.max.threads";

    private static final String DEFAULT_SERVICE_HOST = "localhost";
    private static final int DEFAULT_SERVICE_PORT = 8080;
    private static final int DEFAULT_MAX_THREADS = 200;

    private final Properties properties;

    @SuppressWarnings("unused")
    public Configuration() {
        this(System.getProperties());
    }

    @VisibleForTesting
    Configuration(Properties properties) {
        this.properties = properties;
    }

    public String getHost() {
        return getPropertyOrDefault(SERVICE_HOST, DEFAULT_SERVICE_HOST, String::valueOf);
    }

    public int getPort() {
        return getPropertyOrDefault(SERVICE_PORT, DEFAULT_SERVICE_PORT, Integer::parseInt);
    }

    public int getMaxThreads() {
        return getPropertyOrDefault(SERVICE_MAX_THREADS, DEFAULT_MAX_THREADS, Integer::parseInt);
    }

    private <T> T getPropertyOrDefault(String propertyName, T defaultValue, Function<String, T> parser) {
        return parser.apply(properties.getProperty(propertyName, String.valueOf(defaultValue)));
    }
}
