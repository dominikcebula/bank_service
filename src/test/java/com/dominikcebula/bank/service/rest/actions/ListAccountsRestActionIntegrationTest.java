package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.BankActionsFacadeInvoker;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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
    private MoneyFactory moneyFactory;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        moneyFactory = injector.getInstance(MoneyFactory.class);
    }

    @Test
    public void shouldListOpenedAccounts() {
        Mockito.when(bankActionsFacadeInvoker.listAccounts()).thenReturn(getAccounts());

        Accounts retrievedAccountsInfo = resetClient().getForObject(ACCOUNT_LIST_URI, Accounts.class);

        assertThat(retrievedAccountsInfo.getAccounts())
                .containsOnly(getAccountsList());
        assertEquals(retrievedAccountsInfo.getTotalDeposit().intValue(), TOTAL_DEPOSIT);
    }

    private Accounts getAccounts() {
        return new Accounts().accounts(Arrays.asList(getAccountsList()));
    }

    private Account[] getAccountsList() {
        return new Account[]{
                new Account().accountId("1").balance(moneyFactory.create(100).getNumberStripped()),
                new Account().accountId("2").balance(moneyFactory.create(200).getNumberStripped()),
                new Account().accountId("3").balance(moneyFactory.create(300).getNumberStripped()),
        };
    }
}