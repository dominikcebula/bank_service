package com.dominikcebula.bank.service.configuration;

import java.util.Properties;
import java.util.function.Function;

public class Configuration {

    private static final int DEFAULT_SERVICE_PORT = 8080;
    private static final int DEFAULT_MAX_THREADS = 200;
    private static final String DEFAULT_CURRENCY = "USD";

    private static final Configuration INSTANCE = new Configuration();

    private final Properties properties = System.getProperties();

    private Configuration() {
    }

    public static Configuration getInstance() {
        return INSTANCE;
    }

    public int getPort() {
        return getPropertyOrDefault("service.port", DEFAULT_SERVICE_PORT, Integer::parseInt);
    }

    public int getMaxThreads() {
        return getPropertyOrDefault("service.max.threads", DEFAULT_MAX_THREADS, Integer::parseInt);
    }

    public String getCurrency() {
        return getPropertyOrDefault("service.currency", DEFAULT_CURRENCY, String::valueOf);
    }

    private <T> T getPropertyOrDefault(String propertyName, T defaultValue, Function<String, T> parser) {
        return parser.apply(properties.getProperty(propertyName, String.valueOf(defaultValue)));
    }
}
