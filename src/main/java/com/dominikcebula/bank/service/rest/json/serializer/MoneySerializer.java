package com.dominikcebula.bank.service.rest.json.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.javamoney.moneta.Money;

import java.lang.reflect.Type;

public class MoneySerializer implements JsonSerializer<Money> {
    @Override
    public JsonElement serialize(Money money, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(money.toString());
    }
}
