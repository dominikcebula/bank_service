package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;
import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ListAccountsResponse;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.dominikcebula.bank.service.rest.actions.ListAccountsRestAction.ACCOUNT_LIST_URI;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ListAccountsRestActionIntegrationTest extends SparkRestServerAwareTest {

    private static final int TOTAL_DEPOSIT = 600;

    @Mock
    @Bind
    private BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Test
    public void shouldListOpenedAccounts() {
        Mockito.when(bankActionsFacadeInvoker.listAccounts()).thenReturn(getAccounts());

        ListAccountsResponse listAccountsResponse = resetClient().getForObject(ACCOUNT_LIST_URI, ListAccountsResponse.class);

        assertThat(listAccountsResponse.getAccounts().getAccounts())
                .containsOnly(getAccountsList());
        assertEquals(listAccountsResponse.getAccounts().getTotalDeposit().intValue(), TOTAL_DEPOSIT);
        assertEquals(listAccountsResponse.getStatus().getCode(), ApiCode.ACCOUNTS_LISTED);
    }

    private Accounts getAccounts() {
        return new Accounts()
                .accounts(Arrays.asList(getAccountsList()))
                .totalDeposit(BigDecimal.valueOf(600));
    }

    private Account[] getAccountsList() {
        return new Account[]{
                new Account().accountId("1").balance(BigDecimal.valueOf(100)),
                new Account().accountId("2").balance(BigDecimal.valueOf(200)),
                new Account().accountId("3").balance(BigDecimal.valueOf(300)),
        };
    }
}