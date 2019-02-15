package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.TransferMoneyRequest;
import com.dominikcebula.bank.service.dto.TransferMoneyResponse;
import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.fest.assertions.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static com.dominikcebula.bank.service.rest.ds.response.Response.Status.ERROR;
import static com.dominikcebula.bank.service.rest.ds.response.Response.Status.SUCCESS;
import static com.dominikcebula.bank.service.rest.validator.validators.TransferMoneyRequestValidator.MESSAGE_AMOUNT_VALUE_INCORRECT;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TransferMoneyRestActionIntegrationTest extends SparkRestServerAwareTest {

    private static final AccountId FROM = AccountId.createAccountNumber("1");
    private static final AccountId TO = AccountId.createAccountNumber("2");

    @Bind
    @Mock
    private BankActionsFacadeInvoker bankActionsFacadeInvoker;

    private MoneyFactory moneyFactory;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        moneyFactory = injector.getInstance(MoneyFactory.class);
    }

    @Test
    public void shouldTransferMoney() {
        Money amount = moneyFactory.create(600);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM.getAccountNumber());
        transferMoneyRequest.setTo(TO.getAccountNumber());
        transferMoneyRequest.setAmount(amount.getNumberStripped());

        TransferMoneyResponse transferMoneyResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, transferMoneyRequest,
                TransferMoneyRequest.class, TransferMoneyResponse.class
        );

        assertEquals(ApiCode.MONEY_TRANSFERED, transferMoneyResponse.getStatus().getCode());
        assertEquals(FROM.getAccountNumber(), transferMoneyResponse.getFrom());
        assertEquals(TO.getAccountNumber(), transferMoneyResponse.getTo());
        assertEquals(amount.getNumberStripped(), transferMoneyResponse.getAmount());
    }

    @Test
    public void shouldFailToTransferMoney() throws TransferException {
        Money amount = moneyFactory.create(600);

        Mockito.doThrow(new IllegalArgumentException("TEST"))
                .when(bankActionsFacadeInvoker).transfer(FROM, TO, amount.getNumberStripped());

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM.getAccountNumber());
        transferMoneyRequest.setTo(TO.getAccountNumber());
        transferMoneyRequest.setAmount(amount.getNumberStripped());

        ErrorResponse errorResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, transferMoneyRequest,
                TransferMoneyRequest.class, ErrorResponse.class
        );

        assertEquals(ERROR, errorResponse.getStatus());
    }

    @Test
    public void shouldFailTransferMoneyValidation() {
        Money amount = moneyFactory.create(0);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM.getAccountNumber());
        transferMoneyRequest.setTo(TO.getAccountNumber());
        transferMoneyRequest.setAmount(amount.getNumberStripped());

        ErrorResponse errorResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, transferMoneyRequest,
                TransferMoneyRequest.class, ErrorResponse.class
        );

        assertEquals(ERROR, errorResponse.getStatus());
        assertEquals(MESSAGE_AMOUNT_VALUE_INCORRECT, errorResponse.getMessage());
    }
}