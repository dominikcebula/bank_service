package com.dominikcebula.bank.service.bls.utils;

import com.dominikcebula.bank.service.configuration.Configuration;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MoneyFactoryTest {

    private static final int AMOUNT = 50;
    private static final String EUR = "EUR";

    @Mock
    private Configuration configuration;
    @InjectMocks
    private MoneyFactory moneyFactory;

    @Test
    public void shouldCreateMoneyWithCurrencyFromConfiguration() {
        Mockito.when(configuration.getCurrency()).thenReturn(EUR);

        Money money = moneyFactory.create(AMOUNT);

        assertEquals(AMOUNT, money.getNumber().intValue());
        assertEquals(EUR, money.getCurrency().getCurrencyCode());
    }
}