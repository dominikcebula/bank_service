package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.OpenAccountAction;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.rest.ds.request.AccountOpenRequest;
import com.dominikcebula.bank.service.rest.ds.response.AccountOpenResponse;
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
import static com.dominikcebula.bank.service.rest.validator.validators.AccountOpenRequestValidator.MESSAGE_DEPOSIT_INCORRECT;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OpenAccountRestActionIntegrationTest extends SparkRestServerAwareTest {

    private static final int DEPOSIT = 500;
    private static final AccountId ACCOUNT_ID = AccountId.createAccountNumber("5");

    @Mock
    @Bind
    private OpenAccountAction openAccountAction;
    private MoneyFactory moneyFactory;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        moneyFactory = injector.getInstance(MoneyFactory.class);
    }

    @Test
    public void shouldOpenAccount() throws AccountOpenException {
        Money initialDeposit = moneyFactory.create(DEPOSIT);
        Mockito.when(openAccountAction.openAccount(initialDeposit)).thenReturn(ACCOUNT_ID);

        AccountOpenRequest accountOpenRequest = new AccountOpenRequest();

        accountOpenRequest.setInitialDeposit(initialDeposit);

        AccountOpenResponse accountOpenResponse = resetClient().postForObject(
                OpenAccountRestAction.ACCOUTS_OPEN_URI, accountOpenRequest,
                AccountOpenRequest.class, AccountOpenResponse.class
        );

        assertEquals(SUCCESS, accountOpenResponse.getStatus());
        assertEquals(ACCOUNT_ID, accountOpenResponse.getAccountId());
        Assertions.assertThat(accountOpenResponse.getMessage())
                .contains(ACCOUNT_ID.getAccountNumber());
    }

    @Test
    public void shouldFailedToOpenAccount() throws AccountOpenException {
        Money initialDeposit = moneyFactory.create(DEPOSIT);
        Mockito.when(openAccountAction.openAccount(initialDeposit)).thenThrow(new IllegalArgumentException("TEST"));

        AccountOpenRequest accountOpenRequest = new AccountOpenRequest();

        accountOpenRequest.setInitialDeposit(initialDeposit);

        ErrorResponse errorResponse = resetClient().postForObject(
                OpenAccountRestAction.ACCOUTS_OPEN_URI, accountOpenRequest,
                AccountOpenRequest.class, ErrorResponse.class
        );

        assertEquals(ERROR, errorResponse.getStatus());
    }

    @Test
    public void shouldFailValidationDuringOpenAction() throws AccountOpenException {
        Money initialDeposit = moneyFactory.create(0);

        AccountOpenRequest accountOpenRequest = new AccountOpenRequest();
        accountOpenRequest.setInitialDeposit(initialDeposit);

        ErrorResponse errorResponse = resetClient().postForObject(
                OpenAccountRestAction.ACCOUTS_OPEN_URI, accountOpenRequest,
                AccountOpenRequest.class, ErrorResponse.class
        );

        assertEquals(ERROR, errorResponse.getStatus());
        assertEquals(MESSAGE_DEPOSIT_INCORRECT, errorResponse.getMessage());
    }
}