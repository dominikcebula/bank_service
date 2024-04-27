package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.rest.validator.Validator;

import java.math.BigDecimal;

public class AmountValidator extends Validator<BigDecimal> {

    private static final String AMOUNT_VALUE_PATTERN = "^([0-9]+)|([0-9]+\\.[0-9]{1,2})$";

    static final String MESSAGE_VALUE_MISSING = "Amount value is missing";
    public static final String MESSAGE_VALUE_ZERO = "Amount value has to be greater then zero";
    static final String MESSAGE_VALUE_NOT_MATCHING_PATTERN = "Amount value has to match pattern " + AMOUNT_VALUE_PATTERN;


    @Override
    public void validate(BigDecimal value) {
        String stringValue = String.valueOf(value);

        assertConditionMet(
                value != null,
                MESSAGE_VALUE_MISSING
        );

        assertConditionMet(
                value.compareTo(BigDecimal.ZERO) > 0,
                MESSAGE_VALUE_ZERO
        );

        assertConditionMet(
                stringValue.matches(AMOUNT_VALUE_PATTERN),
                MESSAGE_VALUE_NOT_MATCHING_PATTERN
        );
    }
}
