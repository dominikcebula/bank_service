package com.dominikcebula.bank.service.guice;

import com.dominikcebula.bank.service.ServiceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.junit.Before;

public abstract class ContextAwareTest {

    protected Injector injector;

    @Before
    public void setUp() {
        injector = Guice.createInjector(getServiceModule());
    }

    private Module getServiceModule() {
        return new ComposedServiceModule(
                new ServiceModule(),
                BoundFieldModule.of(this)
        );
    }
}
