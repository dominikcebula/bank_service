package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.ListAccountsAction;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.ds.AccountInfo;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import com.google.common.collect.Sets;
import com.google.inject.testing.fieldbinder.Bind;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static com.dominikcebula.bank.service.rest.actions.ListAccountsRestAction.ACCOUNT_LIST_URI;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ListAccountsRestActionIntegrationTest extends SparkRestServerAwareTest {

    private static final int TOTAL_DEPOSIT = 600;

    @Mock
    @Bind
    private ListAccountsAction listAccountsAction;
    private MoneyFactory moneyFactory;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        moneyFactory = injector.getInstance(MoneyFactory.class);
    }

    @Test
    public void shouldListOpenedAccounts() {
        AccountsInfo accountsInfo = getAccountsInfo();
        Mockito.when(listAccountsAction.listAccounts()).thenReturn(accountsInfo);

        AccountsInfo retrievedAccountsInfo = resetClient().getForObject(ACCOUNT_LIST_URI, AccountsInfo.class);

        assertThat(retrievedAccountsInfo.getAccountsInfo())
                .containsOnly(getAccounts());
        assertEquals(retrievedAccountsInfo.getTotalDeposit().getNumber().intValueExact(), TOTAL_DEPOSIT);
    }

    private AccountsInfo getAccountsInfo() {
        return new AccountsInfo(
                Sets.newHashSet(getAccounts()),
                getTotalDeposit()
        );
    }

    private AccountInfo[] getAccounts() {
        return new AccountInfo[]{
                new AccountInfo(AccountId.createAccountNumber("1"), moneyFactory.create(100)),
                new AccountInfo(AccountId.createAccountNumber("2"), moneyFactory.create(200)),
                new AccountInfo(AccountId.createAccountNumber("3"), moneyFactory.create(300))
        };
    }

    private Money getTotalDeposit() {
        return moneyFactory.create(TOTAL_DEPOSIT);
    }
}