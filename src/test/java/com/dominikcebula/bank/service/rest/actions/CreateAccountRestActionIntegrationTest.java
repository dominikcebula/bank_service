package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountCreateException;
import com.dominikcebula.bank.service.dto.*;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static com.dominikcebula.bank.service.dto.ApiCode.ACCOUNT_CREATED;
import static com.dominikcebula.bank.service.rest.validator.validators.AccountCreateRequestValidator.MESSAGE_DEPOSIT_INCORRECT;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CreateAccountRestActionIntegrationTest extends SparkRestServerAwareTest {

    private static final BigDecimal DEPOSIT = BigDecimal.valueOf(500);
    private static final AccountId ACCOUNT_ID = AccountId.createAccountNumber("5");

    @Mock
    @Bind
    private BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Test
    public void shouldOpenAccount() throws AccountCreateException {
        Account account = new Account().accountId(ACCOUNT_ID.getAccountNumber());
        Mockito.when(bankActionsFacadeInvoker.createAccount(DEPOSIT)).thenReturn(account);

        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();

        accountCreateRequest.setInitialDeposit(DEPOSIT);

        AccountCreateResponse accountCreateResponse = resetClient().postForObject(
                CreateAccountRestAction.ACCOUNTS_CREATE_URI, accountCreateRequest,
                AccountCreateRequest.class, AccountCreateResponse.class
        );

        assertEquals(ACCOUNT_CREATED, accountCreateResponse.getStatus().getCode());
        assertEquals(ACCOUNT_ID, AccountId.createAccountNumber(accountCreateResponse.getAccount().getAccountId()));
    }

    @Test
    public void shouldFailedToOpenAccount() throws AccountCreateException {
        Mockito.when(bankActionsFacadeInvoker.createAccount(DEPOSIT)).thenThrow(new IllegalArgumentException("TEST"));

        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();

        accountCreateRequest.setInitialDeposit(DEPOSIT);

        ApiErrorResponse errorResponse = resetClient().postForObject(
                CreateAccountRestAction.ACCOUNTS_CREATE_URI, accountCreateRequest,
                AccountCreateRequest.class, ApiErrorResponse.class
        );

        assertEquals(ApiCode.FAILED, errorResponse.getStatus().getCode());
    }

    @Test
    public void shouldFailValidationDuringOpenAction() {
        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(BigDecimal.valueOf(0));

        ApiErrorResponse errorResponse = resetClient().postForObject(
                CreateAccountRestAction.ACCOUNTS_CREATE_URI, accountCreateRequest,
                AccountCreateRequest.class, ApiErrorResponse.class
        );

        assertEquals(ApiCode.FAILED, errorResponse.getStatus().getCode());
        assertEquals(MESSAGE_DEPOSIT_INCORRECT, errorResponse.getMessage());
    }
}