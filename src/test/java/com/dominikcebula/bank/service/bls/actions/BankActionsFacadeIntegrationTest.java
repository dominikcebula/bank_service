package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.dao.AccountDao;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.AccountOpenException;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.bls.utils.AccountIdGenerator;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;
import com.dominikcebula.bank.service.guice.ContextAwareTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class BankActionsFacadeIntegrationTest extends ContextAwareTest {

    private static final AccountId ACCOUNT_ID_1 = AccountId.createAccountNumber("1");
    private static final AccountId ACCOUNT_ID_2 = AccountId.createAccountNumber("2");
    private static final AccountId ACCOUNT_ID_3 = AccountId.createAccountNumber("3");
    private static final AccountId NON_EXISTING_ACCOUNT = AccountId.createAccountNumber("4");
    private static final int BALANCE1 = 500;
    private static final int HUGE_AMOUNT = BALANCE1 * 10;
    private static final int BALANCE2 = 600;
    private static final int BALANCE3 = 700;
    private static final int TOTAL_BALANCE = BALANCE1 + BALANCE2 + BALANCE3;
    private static final int NO_MONEY = 0;
    private static final double FLOAT_AMOUNT = 5.671;
    private static final double FLOAT_AMOUNT_ROUNDED = 5.67;

    private BankActionsFacadeInvoker bankActionsFacadeInvoker;
    private AccountDao accountDao;
    @Mock
    @Bind
    private AccountIdGenerator accountIdGenerator;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        super.setUp();
        bankActionsFacadeInvoker = injector.getInstance(BankActionsFacadeInvoker.class);
        accountDao = injector.getInstance(AccountDao.class);
    }

    @Test
    public void shouldOpenAccounts() throws AccountOpenException {
        openAccounts();

        assertThat(accountDao.findAccountIdentifiers())
                .containsOnly(ACCOUNT_ID_1, ACCOUNT_ID_2, ACCOUNT_ID_3);
    }

    @Test
    public void shouldListAccounts() throws AccountOpenException {
        openAccounts();

        Accounts accounts = bankActionsFacadeInvoker.listAccounts();

        assertThat(accounts.getAccounts())
                .containsOnly(
                        new Account().accountId(ACCOUNT_ID_1.getAccountNumber()).balance(money(BALANCE1)),
                        new Account().accountId(ACCOUNT_ID_2.getAccountNumber()).balance(money(BALANCE2)),
                        new Account().accountId(ACCOUNT_ID_3.getAccountNumber()).balance(money(BALANCE3))
                );
        assertEquals(TOTAL_BALANCE, accounts.getTotalDeposit().intValue());
    }

    @Test
    public void shouldListAccountsWhenNonOpened() {

        Accounts accounts = bankActionsFacadeInvoker.listAccounts();

        assertTrue(accounts.getAccounts().isEmpty());
        assertEquals(NO_MONEY, accounts.getTotalDeposit().intValue());
    }

    @Test
    public void shouldTransferMoneyBetweenAccounts() throws AccountOpenException, TransferException {
        openAccounts();

        bankActionsFacadeInvoker.transfer(ACCOUNT_ID_1, ACCOUNT_ID_2, money(BALANCE1));

        assertEquals(NO_MONEY, accountDao.findAccount(ACCOUNT_ID_1).getBalance().intValue());
        assertEquals(BALANCE1 + BALANCE2, accountDao.findAccount(ACCOUNT_ID_2).getBalance().intValue());
        assertEquals(BALANCE3, accountDao.findAccount(ACCOUNT_ID_3).getBalance().intValue());
    }

    @Test
    public void shouldTransferRoundedMoneyBetweenAccounts() throws AccountOpenException, TransferException {
        openAccounts();

        bankActionsFacadeInvoker.transfer(ACCOUNT_ID_1, ACCOUNT_ID_2, BigDecimal.valueOf(FLOAT_AMOUNT));

        assertEquals(BALANCE2 + FLOAT_AMOUNT_ROUNDED, accountDao.findAccount(ACCOUNT_ID_2).getBalance().floatValue(), 0f);
    }

    @Test
    public void shouldFailTransferBecauseOfMissingAccount() throws AccountOpenException, TransferException {
        expectedException.expect(TransferException.class);
        expectedException.expectMessage("Unable to locate account [" + NON_EXISTING_ACCOUNT + "]");

        openAccounts();

        bankActionsFacadeInvoker.transfer(NON_EXISTING_ACCOUNT, ACCOUNT_ID_2, money(BALANCE1));
    }

    @Test
    public void shouldFailTransferBecauseOfNoEnoughFunds() throws AccountOpenException, TransferException {
        expectedException.expect(TransferException.class);
        expectedException.expectMessage("Unable to withdraw amount [" + money(HUGE_AMOUNT) + "] from account that has balance [" + money(BALANCE1) + "]");

        openAccounts();

        bankActionsFacadeInvoker.transfer(ACCOUNT_ID_1, ACCOUNT_ID_2, money(HUGE_AMOUNT));
    }

    private void openAccounts() throws AccountOpenException {
        mockAccountId(ACCOUNT_ID_1);
        bankActionsFacadeInvoker.openAccount(money(BALANCE1));

        mockAccountId(ACCOUNT_ID_2);
        bankActionsFacadeInvoker.openAccount(money(BALANCE2));

        mockAccountId(ACCOUNT_ID_3);
        bankActionsFacadeInvoker.openAccount(money(BALANCE3));
    }

    private BigDecimal money(int amount) {
        return BigDecimal.valueOf(amount);
    }

    private void mockAccountId(AccountId accountId) throws AccountOpenException {
        Mockito.when(accountIdGenerator.generateAccountId(Mockito.anySet()))
                .thenReturn(accountId);
    }
}