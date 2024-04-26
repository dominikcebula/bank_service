package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

import java.util.Set;

public class JavaBeanValidator<T> extends Validator<T> {
    private final jakarta.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public void validate(T value) throws ValidatorException {
        if (value != null) {
            Set<ConstraintViolation<T>> violations = validator.validate(value);

            if (violations.size() > 0) {
                String violationMessage = violations.stream()
                        .map(this::getMessage)
                        .reduce(this::joinMessages)
                        .get();

                throw new ValidatorException(violationMessage);
            }
        }
    }

    private String getMessage(ConstraintViolation violation) {
        return String.format("Field [%s]: %s", violation.getPropertyPath(), violation.getMessage());
    }

    private String joinMessages(String a, String b) {
        return String.join(", ", a, b);
    }
}
