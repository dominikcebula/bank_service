package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.dto.*;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static com.dominikcebula.bank.service.dto.ApiCode.ACCOUNT_OPENED;
import static com.dominikcebula.bank.service.rest.validator.validators.AccountOpenRequestValidator.MESSAGE_DEPOSIT_INCORRECT;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OpenAccountRestActionIntegrationTest extends SparkRestServerAwareTest {

    private static final BigDecimal DEPOSIT = BigDecimal.valueOf(500);
    private static final AccountId ACCOUNT_ID = AccountId.createAccountNumber("5");

    @Mock
    @Bind
    private BankActionsFacadeInvoker bankActionsFacadeInvoker;
    private MoneyFactory moneyFactory;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        moneyFactory = injector.getInstance(MoneyFactory.class);
    }

    @Test
    public void shouldOpenAccount() throws AccountOpenException {
        Account account = new Account().accountId(ACCOUNT_ID.getAccountNumber());
        Mockito.when(bankActionsFacadeInvoker.openAccount(DEPOSIT)).thenReturn(account);

        AccountOpenRequest accountOpenRequest = new AccountOpenRequest();

        accountOpenRequest.setInitialDeposit(DEPOSIT);

        AccountOpenResponse accountOpenResponse = resetClient().postForObject(
                OpenAccountRestAction.ACCOUNTS_OPEN_URI, accountOpenRequest,
                AccountOpenRequest.class, AccountOpenResponse.class
        );

        assertEquals(ACCOUNT_OPENED, accountOpenResponse.getStatus().getCode());
        assertEquals(ACCOUNT_ID, AccountId.createAccountNumber(accountOpenResponse.getAccount().getAccountId()));
    }

    @Test
    public void shouldFailedToOpenAccount() throws AccountOpenException {
        Mockito.when(bankActionsFacadeInvoker.openAccount(DEPOSIT)).thenThrow(new IllegalArgumentException("TEST"));

        AccountOpenRequest accountOpenRequest = new AccountOpenRequest();

        accountOpenRequest.setInitialDeposit(DEPOSIT);

        ApiErrorResponse errorResponse = resetClient().postForObject(
                OpenAccountRestAction.ACCOUNTS_OPEN_URI, accountOpenRequest,
                AccountOpenRequest.class, ApiErrorResponse.class
        );

        assertEquals(ApiCode.FAILED, errorResponse.getCode());
    }

    @Test
    public void shouldFailValidationDuringOpenAction() {
        AccountOpenRequest accountOpenRequest = new AccountOpenRequest();
        accountOpenRequest.setInitialDeposit(BigDecimal.valueOf(0));

        ApiErrorResponse errorResponse = resetClient().postForObject(
                OpenAccountRestAction.ACCOUNTS_OPEN_URI, accountOpenRequest,
                AccountOpenRequest.class, ApiErrorResponse.class
        );

        assertEquals(ApiCode.FAILED, errorResponse.getCode());
        assertEquals(MESSAGE_DEPOSIT_INCORRECT, errorResponse.getMessage());
    }
}