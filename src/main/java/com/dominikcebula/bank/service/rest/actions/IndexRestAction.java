package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.google.inject.Inject;

public class IndexRestAction extends AbstractRestAction<Void, String> {

    public static final String INDEX_ACTION_URI = "/";
    static final String MESSAGE_SERVIE_UP = "Bank Service is running";

    @Inject
    public IndexRestAction(GsonProvider gsonProvider) {
        super(gsonProvider, Void.class, String.class);
    }

    @Override
    String handleRequest(Void request) {
        return MESSAGE_SERVIE_UP;
    }
}
