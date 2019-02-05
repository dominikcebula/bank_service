package com.dominikcebula.bank.service.rest.json;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.rest.json.deserializer.AccountIdDeserializer;
import com.dominikcebula.bank.service.rest.json.deserializer.MoneyDeserializer;
import com.dominikcebula.bank.service.rest.json.serializer.AccountIdSerializer;
import com.dominikcebula.bank.service.rest.json.serializer.MoneySerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import org.javamoney.moneta.Money;

public class GsonProvider {

    private final MoneySerializer moneySerializer;
    private final MoneyDeserializer moneyDeserializer;
    private final AccountIdSerializer accountIdSerializer;
    private final AccountIdDeserializer accountIdDeserializer;

    @Inject
    public GsonProvider(MoneySerializer moneySerializer, MoneyDeserializer moneyDeserializer, AccountIdSerializer accountIdSerializer, AccountIdDeserializer accountIdDeserializer) {
        this.moneySerializer = moneySerializer;
        this.moneyDeserializer = moneyDeserializer;
        this.accountIdSerializer = accountIdSerializer;
        this.accountIdDeserializer = accountIdDeserializer;
    }

    public Gson provide() {
        return new GsonBuilder()
                .registerTypeHierarchyAdapter(Money.class, moneySerializer)
                .registerTypeHierarchyAdapter(Money.class, moneyDeserializer)
                .registerTypeHierarchyAdapter(AccountId.class, accountIdSerializer)
                .registerTypeHierarchyAdapter(AccountId.class, accountIdDeserializer)
                .create();
    }
}
