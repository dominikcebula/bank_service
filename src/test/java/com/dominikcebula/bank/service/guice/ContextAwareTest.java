package com.dominikcebula.bank.service.guice;

import com.dominikcebula.bank.service.ServiceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.junit.jupiter.api.BeforeEach;


public abstract class ContextAwareTest {

    protected Injector injector;

    @BeforeEach
    protected void setUp() {
        injector = Guice.createInjector(getComposedServiceModule());
    }

    private Module getComposedServiceModule() {
        return new ComposedServiceModule(
                getServiceModule(),
                BoundFieldModule.of(this)
        );
    }

    protected ServiceModule getServiceModule() {
        return new ServiceModule();
    }
}
