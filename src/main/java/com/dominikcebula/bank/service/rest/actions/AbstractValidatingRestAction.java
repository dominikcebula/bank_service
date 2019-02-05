package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.validator.Validator;
import com.google.gson.Gson;

abstract class AbstractValidatingRestAction<I, R> extends AbstractRestAction<I, R> {

    AbstractValidatingRestAction(Gson gson, Class<I> requestClass, Class<R> responseClass) {
        super(gson, requestClass, responseClass);
    }

    @Override
    void postProcessRequest(I requestObject) throws Exception {
        getRequestValidator().validate(requestObject);
    }

    abstract Validator<I> getRequestValidator();
}
