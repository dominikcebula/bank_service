package com.dominikcebula.bank.service.rest.validator.validators;

import com.dominikcebula.bank.service.rest.validator.exception.ValidatorException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static com.dominikcebula.bank.service.rest.validator.validators.AmountValidator.MESSAGE_VALUE_INCORRECT;
import static com.dominikcebula.bank.service.rest.validator.validators.AmountValidator.MESSAGE_VALUE_MISSING;

@RunWith(JUnitParamsRunner.class)
public class AmountValidatorTest {

    private final AmountValidator amountValidator = new AmountValidator();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    @Parameters({
            "1",
            "5",
            "10",
            "10000",
            "0.1",
            "0.01",
            "0.10",
            "0.50",
            "0.99",
            "100.56",
            "9.99",
            "9.00"
    })
    public void shouldReportCorrectAmount(BigDecimal amount) throws ValidatorException {
        amountValidator.validate(amount);
    }

    @Test
    @Parameters({
            "-1",
            "-5",
            "0",
            "0.123",
            "5.999",
            "10500.5914"
    })
    public void shouldReportIncorrectAmount(BigDecimal amount) throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_VALUE_INCORRECT);

        amountValidator.validate(amount);
    }

    @Test
    public void shouldReportMissingAmount() throws ValidatorException {
        expectedException.expect(ValidatorException.class);
        expectedException.expectMessage(MESSAGE_VALUE_MISSING);

        amountValidator.validate(null);
    }
}