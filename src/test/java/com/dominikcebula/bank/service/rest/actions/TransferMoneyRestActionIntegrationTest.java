package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.TransferMoneyAction;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.rest.ds.request.TransferMoneyRequest;
import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;
import com.dominikcebula.bank.service.rest.ds.response.TransferMoneyResponse;
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
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TransferMoneyRestActionIntegrationTest extends SparkRestServerAwareTest {

    private static final AccountId FROM = AccountId.createAccountNumber("1");
    private static final AccountId TO = AccountId.createAccountNumber("2");

    @Bind
    @Mock
    private TransferMoneyAction transferMoneyAction;

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
        transferMoneyRequest.setFrom(FROM);
        transferMoneyRequest.setTo(TO);
        transferMoneyRequest.setAmount(amount);

        TransferMoneyResponse transferMoneyResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, transferMoneyRequest,
                TransferMoneyRequest.class, TransferMoneyResponse.class
        );

        assertEquals(SUCCESS, transferMoneyResponse.getStatus());
        assertEquals(FROM, transferMoneyResponse.getFrom());
        assertEquals(TO, transferMoneyResponse.getTo());
        assertEquals(amount, transferMoneyResponse.getAmount());
        Assertions.assertThat(transferMoneyResponse.getMessage())
                .contains(FROM.getAccountNumber())
                .contains(TO.getAccountNumber())
                .contains(String.valueOf(amount.getNumber().intValue()));
    }

    @Test
    public void shouldFailToTransferMoney() throws TransferException {
        Money amount = moneyFactory.create(600);

        Mockito.doThrow(new IllegalArgumentException("TEST"))
                .when(transferMoneyAction).transfer(FROM, TO, amount);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM);
        transferMoneyRequest.setTo(TO);
        transferMoneyRequest.setAmount(amount);

        ErrorResponse errorResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, transferMoneyRequest,
                TransferMoneyRequest.class, ErrorResponse.class
        );

        assertEquals(ERROR, errorResponse.getStatus());
    }
}