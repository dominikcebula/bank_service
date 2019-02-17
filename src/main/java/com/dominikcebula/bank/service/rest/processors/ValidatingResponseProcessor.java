package com.dominikcebula.bank.service.rest.processors;

import com.dominikcebula.bank.service.rest.validator.Validator;

public class ValidatingResponseProcessor<T> implements ResponseProcessor<T> {

    private final Validator<T> validator;

    public ValidatingResponseProcessor(Validator<T> validator) {
        this.validator = validator;
    }

    @Override
    public void process(T requestObject) throws Exception {
        validator.validate(requestObject);
    }
}
