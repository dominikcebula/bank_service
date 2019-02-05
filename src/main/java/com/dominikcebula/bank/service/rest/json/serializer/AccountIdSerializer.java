package com.dominikcebula.bank.service.rest.json.serializer;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AccountIdSerializer implements JsonSerializer<AccountId> {
    @Override
    public JsonElement serialize(AccountId accountId, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(accountId.getAccountNumber());
    }
}
