package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;

import java.math.BigDecimal;

public class AmountValidator extends Validator<BigDecimal> {

    public static final String MESSAGE_VALUE_MISSING = "Amount value is missing";
    public static final String MESSAGE_VALUE_INCORRECT = "Amount value is incorrect";

    @Override
    public void validate(BigDecimal value) throws ValidatorException {
        String stringValue = String.valueOf(value);

        assertConditionMet(
                value != null,
                MESSAGE_VALUE_MISSING
        );

        assertConditionMet(
                value.compareTo(BigDecimal.ZERO) > 0,
                MESSAGE_VALUE_INCORRECT
        );

        assertConditionMet(
                stringValue.matches("^([0-9]+)|([0-9]+\\.[0-9]{1,2})$"),
                MESSAGE_VALUE_INCORRECT
        );
    }
}
