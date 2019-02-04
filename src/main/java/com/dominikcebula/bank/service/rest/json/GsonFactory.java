package com.dominikcebula.bank.service.rest.json;

import com.dominikcebula.bank.service.rest.json.serializer.MoneySerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.javamoney.moneta.Money;

public class GsonFactory {
    public Gson create() {
        return new GsonBuilder()
                .registerTypeHierarchyAdapter(Money.class, new MoneySerializer())
                .create();
    }
}
