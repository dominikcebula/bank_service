package com.dominikcebula.bank.service.rest.json.deserializer;

import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.inject.Inject;
import org.javamoney.moneta.Money;

import java.lang.reflect.Type;

public class MoneyDeserializer implements JsonDeserializer<Money> {

    private final MoneyFactory moneyFactory;

    @Inject
    public MoneyDeserializer(MoneyFactory moneyFactory) {
        this.moneyFactory = moneyFactory;
    }

    @Override
    public Money deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return moneyFactory.create(jsonElement.getAsFloat());
    }
}
