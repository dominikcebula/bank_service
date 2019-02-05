package com.dominikcebula.bank.service.rest.actions;

import com.google.gson.Gson;

public class IndexRestAction extends AbstractRestAction<Void, String> {

    public IndexRestAction(Gson gson) {
        super(gson, Void.class, String.class);
    }

    @Override
    String handleRequest(Void request) {
        return "Bank Service is running";
    }
}
