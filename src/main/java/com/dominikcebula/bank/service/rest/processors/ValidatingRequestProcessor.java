package com.dominikcebula.bank.service.rest.processors;

import com.dominikcebula.bank.service.rest.validator.Validator;

public class ValidatingRequestProcessor<T> implements RequestProcessor<T> {

    private final Validator<T> validator;

    public ValidatingRequestProcessor(Validator<T> validator) {
        this.validator = validator;
    }

    @Override
    public void process(T requestObject) {
        validator.validate(requestObject);
    }
}
