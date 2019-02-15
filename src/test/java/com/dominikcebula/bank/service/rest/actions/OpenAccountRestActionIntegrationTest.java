package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.AccountOpenRequest;
import com.dominikcebula.bank.service.dto.AccountOpenResponse;
import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static com.dominikcebula.bank.service.dto.ApiCode.ACCOUNT_OPENED;
import static com.dominikcebula.bank.service.rest.ds.response.Response.Status.ERROR;
import static com.dominikcebula.bank.service.rest.validator.validators.AccountOpenRequestValidator.MESSAGE_DEPOSIT_INCORRECT;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OpenAccountRestActionIntegrationTest extends SparkRestServerAwareTest {

    private static final int DEPOSIT = 500;
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
        BigDecimal initialDeposit = moneyFactory.create(DEPOSIT).getNumberStripped();
        Account account = new Account().accountId(ACCOUNT_ID.getAccountNumber());
        Mockito.when(bankActionsFacadeInvoker.openAccount(initialDeposit)).thenReturn(account);

        AccountOpenRequest accountOpenRequest = new AccountOpenRequest();

        accountOpenRequest.setInitialDeposit(initialDeposit);

        AccountOpenResponse accountOpenResponse = resetClient().postForObject(
                OpenAccountRestAction.ACCOUNTS_OPEN_URI, accountOpenRequest,
                AccountOpenRequest.class, AccountOpenResponse.class
        );

        assertEquals(ACCOUNT_OPENED, accountOpenResponse.getStatus().getCode());
        assertEquals(ACCOUNT_ID, AccountId.createAccountNumber(accountOpenResponse.getAccount().getAccountId()));
    }

    @Test
    public void shouldFailedToOpenAccount() throws AccountOpenException {
        BigDecimal initialDeposit = moneyFactory.create(DEPOSIT).getNumberStripped();
        Mockito.when(bankActionsFacadeInvoker.openAccount(initialDeposit)).thenThrow(new IllegalArgumentException("TEST"));

        AccountOpenRequest accountOpenRequest = new AccountOpenRequest();

        accountOpenRequest.setInitialDeposit(initialDeposit);

        ErrorResponse errorResponse = resetClient().postForObject(
                OpenAccountRestAction.ACCOUNTS_OPEN_URI, accountOpenRequest,
                AccountOpenRequest.class, ErrorResponse.class
        );

        assertEquals(ERROR, errorResponse.getStatus());
    }

    @Test
    public void shouldFailValidationDuringOpenAction() {
        BigDecimal initialDeposit = moneyFactory.create(0).getNumberStripped();

        AccountOpenRequest accountOpenRequest = new AccountOpenRequest();
        accountOpenRequest.setInitialDeposit(initialDeposit);

        ErrorResponse errorResponse = resetClient().postForObject(
                OpenAccountRestAction.ACCOUNTS_OPEN_URI, accountOpenRequest,
                AccountOpenRequest.class, ErrorResponse.class
        );

        assertEquals(ERROR, errorResponse.getStatus());
        assertEquals(MESSAGE_DEPOSIT_INCORRECT, errorResponse.getMessage());
    }
}