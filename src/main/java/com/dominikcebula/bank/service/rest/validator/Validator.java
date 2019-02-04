package com.dominikcebula.bank.service.rest.validator;

import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;

public abstract class Validator<T> {
    public abstract void validate(T value) throws ValidatorException;

    protected void assertConditionMet(boolean result, String messageIfNotMet) throws ValidatorException {
        if (!result)
            throw new ValidatorException(messageIfNotMet);
    }
}
