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
import static com.dominikcebula.bank.service.rest.validator.validators.AmountValidator.MESSAGE_VALUE_ZERO;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CreateAccountRestActionIntegrationTest extends SparkRestServerAwareTest {

    private static final BigDecimal DEPOSIT = BigDecimal.valueOf(5000.85);
    private static final AccountId ACCOUNT_ID = AccountId.createRandomAccountId();

    @Mock
    @Bind
    private BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Test
    public void shouldCreateAccount() throws AccountCreateException {
        Account account = new Account().accountId(ACCOUNT_ID.getAccountNumber()).balance(DEPOSIT);
        Mockito.when(bankActionsFacadeInvoker.createAccount(DEPOSIT)).thenReturn(account);

        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();

        accountCreateRequest.setInitialDeposit(DEPOSIT);

        AccountCreateResponse accountCreateResponse = resetClient().postForObject(
                CreateAccountRestAction.ACCOUNTS_CREATE_URI, accountCreateRequest,
                AccountCreateRequest.class, AccountCreateResponse.class
        );

        Account createdAccount = accountCreateResponse.getAccount();
        assertEquals(ACCOUNT_CREATED, accountCreateResponse.getStatus().getCode());
        assertEquals(ACCOUNT_ID, AccountId.createAccountNumber(createdAccount.getAccountId()));
        assertEquals(DEPOSIT, createdAccount.getBalance());
    }

    @Test
    public void shouldFailedToCreateAccount() throws AccountCreateException {
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
    public void shouldFailValidationDuringCreateAction() {
        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(BigDecimal.valueOf(0));

        ApiErrorResponse errorResponse = resetClient().postForObject(
                CreateAccountRestAction.ACCOUNTS_CREATE_URI, accountCreateRequest,
                AccountCreateRequest.class, ApiErrorResponse.class
        );

        assertEquals(ApiCode.FAILED, errorResponse.getStatus().getCode());
        assertEquals(MESSAGE_VALUE_ZERO, errorResponse.getMessage());
    }

    @Test
    public void shouldFailBeanValidationDuringCreateAction() {
        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();

        ApiErrorResponse errorResponse = resetClient().postForObject(
                CreateAccountRestAction.ACCOUNTS_CREATE_URI, accountCreateRequest,
                AccountCreateRequest.class, ApiErrorResponse.class
        );

        assertEquals(ApiCode.FAILED, errorResponse.getStatus().getCode());
        assertEquals("Field [initialDeposit]: must not be null", errorResponse.getMessage());
    }
}