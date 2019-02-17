package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.processors.RequestProcessor;
import com.dominikcebula.bank.service.rest.processors.ResponseProcessor;
import com.dominikcebula.bank.service.rest.processors.ValidatingRequestProcessor;
import com.dominikcebula.bank.service.rest.processors.ValidatingResponseProcessor;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.JavaBeanValidator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

abstract class AbstractValidatingRestAction<I, R> extends AbstractRestAction<I, R> {

    AbstractValidatingRestAction(Class<I> requestClass, Class<R> responseClass) {
        super(requestClass, responseClass);
    }

    @Override
    List<RequestProcessor<I>> getRequestProcessors() {
        return Arrays.asList(
                new ValidatingRequestProcessor<>(getJavaBeanValidator()),
                new ValidatingRequestProcessor<>(getRequestValidator())
        );
    }

    @Override
    List<ResponseProcessor<R>> getResponseProcessors() {
        return Collections.singletonList(
                new ValidatingResponseProcessor<>(getJavaBeanValidator())
        );
    }

    private <T> JavaBeanValidator<T> getJavaBeanValidator() {
        return new JavaBeanValidator<>();
    }

    abstract Validator<I> getRequestValidator();
}
