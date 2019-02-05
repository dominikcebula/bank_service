package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.dominikcebula.bank.service.rest.validator.Validator;

abstract class AbstractValidatingRestAction<I, R> extends AbstractRestAction<I, R> {

    AbstractValidatingRestAction(GsonProvider gsonProvider, Class<I> requestClass, Class<R> responseClass) {
        super(gsonProvider, requestClass, responseClass);
    }

    @Override
    void postProcessRequest(I requestObject) throws Exception {
        getRequestValidator().validate(requestObject);
    }

    abstract Validator<I> getRequestValidator();
}
