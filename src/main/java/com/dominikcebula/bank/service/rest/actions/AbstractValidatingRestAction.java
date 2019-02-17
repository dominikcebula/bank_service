package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.JavaBeanValidator;

abstract class AbstractValidatingRestAction<I, R> extends AbstractRestAction<I, R> {

    AbstractValidatingRestAction(Class<I> requestClass, Class<R> responseClass) {
        super(requestClass, responseClass);
    }

    @Override
    void postProcessRequestObject(I requestObject) throws Exception {
        getJavaBeanValidator().validate(requestObject);
        getRequestValidator().validate(requestObject);
    }

    @Override
    void postProcessResponseObject(R responseObject) throws Exception {
        getJavaBeanValidator().validate(responseObject);
    }

    private <T> JavaBeanValidator<T> getJavaBeanValidator() {
        return new JavaBeanValidator<>();
    }

    abstract Validator<I> getRequestValidator();
}
