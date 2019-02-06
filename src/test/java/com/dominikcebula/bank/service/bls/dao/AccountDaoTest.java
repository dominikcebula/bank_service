package com.dominikcebula.bank.service.bls.dao;

import com.dominikcebula.bank.service.bls.ds.Account;
import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.ds.AccountInfo;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class AccountDaoTest {

    private AccountDao accountDao;

    @Before
    public void setUp() {
        accountDao = new AccountDao();
    }

    @Test
    public void shouldBeEmpty() {
        assertTrue(accountDao.findAllAccounts().isEmpty());
        assertTrue(accountDao.findAllAccountsInfo().isEmpty());
        assertTrue(accountDao.findAccountIdentifiers().isEmpty());
        assertFalse(accountDao.accountExists(ACCOUNT1_ID));
    }

    @Test
    public void shouldStoreOneAccount() {
        accountDao.store(ACCOUNT1_ID, ACCOUNT1);

        assertSame(ACCOUNT1, accountDao.findAccount(ACCOUNT1_ID));
    }

    @Test
    public void shouldStoreThreeAccount() {
        accountDao.store(ACCOUNT1_ID, ACCOUNT1);
        accountDao.store(ACCOUNT2_ID, ACCOUNT2);
        accountDao.store(ACCOUNT3_ID, ACCOUNT3);

        assertSame(ACCOUNT1, accountDao.findAccount(ACCOUNT1_ID));
        assertSame(ACCOUNT2, accountDao.findAccount(ACCOUNT2_ID));
        assertSame(ACCOUNT3, accountDao.findAccount(ACCOUNT3_ID));
    }

    @Test
    public void shouldCorrectlyCheckForExistence() {
        accountDao.store(ACCOUNT1_ID, ACCOUNT1);
        accountDao.store(ACCOUNT3_ID, ACCOUNT3);

        assertSame(ACCOUNT1, accountDao.findAccount(ACCOUNT1_ID));
        assertTrue(accountDao.accountExists(ACCOUNT1_ID));
        assertFalse(accountDao.accountExists(ACCOUNT2_ID));
        assertSame(ACCOUNT3, accountDao.findAccount(ACCOUNT3_ID));
    }

    @Test
    public void shouldCorrectlyFetchAccountIdentifiers() {
        accountDao.store(ACCOUNT1_ID, ACCOUNT1);
        accountDao.store(ACCOUNT2_ID, ACCOUNT2);

        assertThat(accountDao.findAccountIdentifiers())
                .containsOnly(ACCOUNT1_ID, ACCOUNT2_ID);
    }

    @Test
    public void shouldCorrectlyFindAllAccounts() {
        accountDao.store(ACCOUNT1_ID, ACCOUNT1);
        accountDao.store(ACCOUNT2_ID, ACCOUNT2);

        assertThat(accountDao.findAllAccounts())
                .containsOnly(ACCOUNT1, ACCOUNT2);
    }

    @Test
    public void shouldCorrectlyFindAllAccountsInfo() {
        accountDao.store(ACCOUNT1_ID, ACCOUNT1);
        accountDao.store(ACCOUNT2_ID, ACCOUNT2);

        assertThat(accountDao.findAllAccountsInfo())
                .containsOnly(ACCOUNT1_INFO, ACCOUNT2_INFO);
    }

    private static final Money ACCOUNT1_BALANCE = Money.of(10, "USD");
    private static final Money ACCOUNT2_BALANCE = Money.of(15, "USD");
    private static final Money ACCOUNT3_BALANCE = Money.of(30, "USD");

    private static final AccountId ACCOUNT1_ID = AccountId.createAccountNumber("1");
    private static final AccountId ACCOUNT2_ID = AccountId.createAccountNumber("2");
    private static final AccountId ACCOUNT3_ID = AccountId.createAccountNumber("3");

    private static final AccountInfo ACCOUNT1_INFO = new AccountInfo(ACCOUNT1_ID, ACCOUNT1_BALANCE);
    private static final AccountInfo ACCOUNT2_INFO = new AccountInfo(ACCOUNT2_ID, ACCOUNT2_BALANCE);

    private static final Account ACCOUNT1 = new Account(ACCOUNT1_BALANCE);
    private static final Account ACCOUNT2 = new Account(ACCOUNT2_BALANCE);
    private static final Account ACCOUNT3 = new Account(ACCOUNT3_BALANCE);
}