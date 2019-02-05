package com.dominikcebula.bank.service.bls.utils;

import com.dominikcebula.bank.service.configuration.Configuration;
import org.javamoney.moneta.Money;

import javax.money.Monetary;

public class MoneyFactory {

    private final Configuration configuration;

    public MoneyFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public Money create(Number number) {
        return create(number, configuration.getCurrency());
    }

    private Money create(Number number, String currencyCode) {
        return Money.of(number, currencyCode)
                .with(Monetary.getDefaultRounding());
    }
}
