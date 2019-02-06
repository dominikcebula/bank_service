package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.google.inject.Inject;

public class IndexRestAction extends AbstractRestAction<Void, String> {

    public static final String INDEX_ACTION_URI = "/";

    @Inject
    public IndexRestAction(GsonProvider gsonProvider) {
        super(gsonProvider, Void.class, String.class);
    }

    @Override
    String handleRequest(Void request) {
        return "Bank Service is running";
    }
}
