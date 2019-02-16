package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.validator.Validator;

abstract class AbstractValidatingRestAction<I, R> extends AbstractRestAction<I, R> {

    AbstractValidatingRestAction(Class<I> requestClass, Class<R> responseClass) {
        super(requestClass, responseClass);
    }

    @Override
    void postProcessRequestObject(I requestObject) throws Exception {
        getRequestValidator().validate(requestObject);
    }

    abstract Validator<I> getRequestValidator();
}
