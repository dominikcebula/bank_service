package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacade;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ApiErrorResponse;
import com.dominikcebula.bank.service.dto.MoneyTransfer;
import com.dominikcebula.bank.service.dto.TransferMoneyResponse;
import com.dominikcebula.bank.service.rest.actions.base.NoActionsFacadeInContextIntegrationTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static com.dominikcebula.bank.service.rest.validator.validators.AmountValidator.MESSAGE_VALUE_ZERO;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TransferMoneyRestActionIntegrationTest extends NoActionsFacadeInContextIntegrationTest {

    private static final AccountId FROM = AccountId.createRandomAccountId();
    private static final AccountId TO = AccountId.createRandomAccountId();

    @Bind
    @Mock
    private BankActionsFacade bankActionsFacade;

    @Test
    public void shouldTransferMoney() {
        BigDecimal amount = BigDecimal.valueOf(600);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(amount);

        TransferMoneyResponse transferMoneyResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, moneyTransfer,
                MoneyTransfer.class, TransferMoneyResponse.class
        );

        assertEquals(ApiCode.MONEY_TRANSFERRED, transferMoneyResponse.getStatus().getCode());
        assertEquals(FROM.getAccountNumber(), transferMoneyResponse.getMoneyTransfer().getFrom());
        assertEquals(TO.getAccountNumber(), transferMoneyResponse.getMoneyTransfer().getTo());
        assertEquals(amount, transferMoneyResponse.getMoneyTransfer().getAmount());
    }

    @Test
    public void shouldFailToTransferMoney() throws TransferException {
        BigDecimal amount = BigDecimal.valueOf(600);

        Mockito.doThrow(new IllegalArgumentException("TEST"))
                .when(bankActionsFacade).transfer(FROM, TO, amount);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(amount);

        ApiErrorResponse errorResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, moneyTransfer,
                MoneyTransfer.class, ApiErrorResponse.class
        );

        assertEquals(ApiCode.FAILED, errorResponse.getStatus().getCode());
    }

    @Test
    public void shouldFailTransferMoneyValidation() {
        BigDecimal amount = BigDecimal.valueOf(0);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(FROM.getAccountNumber());
        moneyTransfer.setTo(TO.getAccountNumber());
        moneyTransfer.setAmount(amount);

        ApiErrorResponse errorResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, moneyTransfer,
                MoneyTransfer.class, ApiErrorResponse.class
        );

        assertEquals(ApiCode.FAILED, errorResponse.getStatus().getCode());
        assertEquals(MESSAGE_VALUE_ZERO, errorResponse.getMessage());
    }

    @Test
    public void shouldFailTransferMoneyBeanValidation() {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setTo(TO.getAccountNumber());

        ApiErrorResponse errorResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, moneyTransfer,
                MoneyTransfer.class, ApiErrorResponse.class
        );

        assertEquals(ApiCode.FAILED, errorResponse.getStatus().getCode());
        assertThat(errorResponse.getMessage())
                .contains("Field [from]: must not be null")
                .contains("Field [amount]: must not be null");
    }
}