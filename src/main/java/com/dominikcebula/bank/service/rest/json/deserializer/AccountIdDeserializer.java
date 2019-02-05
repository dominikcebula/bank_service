package com.dominikcebula.bank.service.rest.json.deserializer;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class AccountIdDeserializer implements JsonDeserializer<AccountId> {
    @Override
    public AccountId deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return AccountId.createAccountNumber(jsonElement.getAsString());
    }
}
