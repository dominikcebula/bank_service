package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ApiErrorResponse;
import com.dominikcebula.bank.service.dto.TransferMoneyRequest;
import com.dominikcebula.bank.service.dto.TransferMoneyResponse;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

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
        BigDecimal amount = BigDecimal.valueOf(600);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM.getAccountNumber());
        transferMoneyRequest.setTo(TO.getAccountNumber());
        transferMoneyRequest.setAmount(amount);

        TransferMoneyResponse transferMoneyResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, transferMoneyRequest,
                TransferMoneyRequest.class, TransferMoneyResponse.class
        );

        assertEquals(ApiCode.MONEY_TRANSFERED, transferMoneyResponse.getStatus().getCode());
        assertEquals(FROM.getAccountNumber(), transferMoneyResponse.getFrom());
        assertEquals(TO.getAccountNumber(), transferMoneyResponse.getTo());
        assertEquals(amount, transferMoneyResponse.getAmount());
    }

    @Test
    public void shouldFailToTransferMoney() throws TransferException {
        BigDecimal amount = BigDecimal.valueOf(600);

        Mockito.doThrow(new IllegalArgumentException("TEST"))
                .when(bankActionsFacadeInvoker).transfer(FROM, TO, amount);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM.getAccountNumber());
        transferMoneyRequest.setTo(TO.getAccountNumber());
        transferMoneyRequest.setAmount(amount);

        ApiErrorResponse errorResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, transferMoneyRequest,
                TransferMoneyRequest.class, ApiErrorResponse.class
        );

        assertEquals(ApiCode.FAILED, errorResponse.getCode());
    }

    @Test
    public void shouldFailTransferMoneyValidation() {
        BigDecimal amount = BigDecimal.valueOf(0);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(FROM.getAccountNumber());
        transferMoneyRequest.setTo(TO.getAccountNumber());
        transferMoneyRequest.setAmount(amount);

        ApiErrorResponse errorResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, transferMoneyRequest,
                TransferMoneyRequest.class, ApiErrorResponse.class
        );

        assertEquals(ApiCode.FAILED, errorResponse.getCode());
        assertEquals(MESSAGE_AMOUNT_VALUE_INCORRECT, errorResponse.getMessage());
    }
}