package com.dominikcebula.bank.service.rest.actions;

public class IndexAction extends AbstractRestAction<Void, String> {

    public IndexAction() {
        super(Void.class, String.class);
    }

    @Override
    String handleRequest(Void request) {
        return "Bank Service is running";
    }
}
