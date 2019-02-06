package com.dominikcebula.bank.service.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

import java.util.Arrays;

public class ComposedServiceModule extends AbstractModule {
    private final Module[] modules;

    public ComposedServiceModule(Module... modules) {
        this.modules = modules;
    }

    @Override
    protected void configure() {
        Arrays.stream(modules).forEach(this::install);
    }
}
