package com.dominikcebula.bank.service.db;

import com.dominikcebula.bank.service.application.dao.AccountDao;
import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.dto.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class InMemoryAccountStorageTest {

    private AccountDao accountDao;

    @BeforeEach
    void setUp() {
        accountDao = new InMemoryAccountStorage(new Configuration());
    }

    @Test
    void shouldBeEmpty() {
        assertTrue(accountDao.findAllAccounts().isEmpty());
        assertTrue(accountDao.findAccountIdentifiers().isEmpty());
        assertFalse(accountDao.accountExists(ACCOUNT1_ID));
    }

    @Test
    void shouldStoreOneAccount() throws InterruptedException {
        accountDao.store(ACCOUNT1);

        assertEquals(ACCOUNT1, accountDao.findAccount(ACCOUNT1_ID));
    }

    @Test
    void shouldStoreThreeAccount() throws InterruptedException {
        accountDao.store(ACCOUNT1);
        accountDao.store(ACCOUNT2);
        accountDao.store(ACCOUNT3);

        assertEquals(ACCOUNT1, accountDao.findAccount(ACCOUNT1_ID));
        assertEquals(ACCOUNT2, accountDao.findAccount(ACCOUNT2_ID));
        assertEquals(ACCOUNT3, accountDao.findAccount(ACCOUNT3_ID));
    }

    @Test
    void shouldGiveImmutableAccount() throws InterruptedException {
        accountDao.store(ACCOUNT1);

        accountDao.findAccount(ACCOUNT1_ID).setBalance(BigDecimal.valueOf(555));

        assertEquals(ACCOUNT1_BALANCE, accountDao.findAccount(ACCOUNT1_ID).getBalance());
    }

    @Test
    void shouldGiveImmutableAccounts() throws InterruptedException {
        accountDao.store(ACCOUNT1);

        accountDao.findAllAccounts().getFirst().setBalance(BigDecimal.valueOf(555));

        assertEquals(ACCOUNT1_BALANCE, accountDao.findAllAccounts().getFirst().getBalance());
    }

    @Test
    void shouldReturnNullWhenSearchingNonExistingAccount() {
        Account account = accountDao.findAccount(ACCOUNT1_ID);

        assertNull(account);
    }

    @Test
    void shouldCorrectlyCheckForExistence() throws InterruptedException {
        accountDao.store(ACCOUNT1);
        accountDao.store(ACCOUNT3);

        assertEquals(ACCOUNT1, accountDao.findAccount(ACCOUNT1_ID));
        assertTrue(accountDao.accountExists(ACCOUNT1_ID));
        assertFalse(accountDao.accountExists(ACCOUNT2_ID));
        assertEquals(ACCOUNT3, accountDao.findAccount(ACCOUNT3_ID));
    }

    @Test
    void shouldCorrectlyFetchAccountIdentifiers() throws InterruptedException {
        accountDao.store(ACCOUNT1);
        accountDao.store(ACCOUNT2);

        assertThat(accountDao.findAccountIdentifiers())
                .containsOnly(ACCOUNT1_ID, ACCOUNT2_ID);
    }

    @Test
    void shouldCorrectlyFindAllAccounts() throws InterruptedException {
        accountDao.store(ACCOUNT1);
        accountDao.store(ACCOUNT2);

        assertThat(accountDao.findAllAccounts())
                .containsOnly(ACCOUNT1, ACCOUNT2);
    }

    @Test
    void shouldCorrectlyFindAllAccountsInfo() throws InterruptedException {
        accountDao.store(ACCOUNT1);
        accountDao.store(ACCOUNT2);

        assertThat(accountDao.findAllAccounts())
                .containsOnly(ACCOUNT1, ACCOUNT2);
    }

    private static final BigDecimal ACCOUNT1_BALANCE = BigDecimal.valueOf(10);
    private static final BigDecimal ACCOUNT2_BALANCE = BigDecimal.valueOf(15);
    private static final BigDecimal ACCOUNT3_BALANCE = BigDecimal.valueOf(30);

    private static final AccountId ACCOUNT1_ID = AccountId.createAccountNumber("1");
    private static final AccountId ACCOUNT2_ID = AccountId.createAccountNumber("2");
    private static final AccountId ACCOUNT3_ID = AccountId.createAccountNumber("3");

    private static final Account ACCOUNT1 = new Account().accountId(ACCOUNT1_ID.getAccountNumber()).balance(ACCOUNT1_BALANCE);
    private static final Account ACCOUNT2 = new Account().accountId(ACCOUNT2_ID.getAccountNumber()).balance(ACCOUNT2_BALANCE);
    private static final Account ACCOUNT3 = new Account().accountId(ACCOUNT3_ID.getAccountNumber()).balance(ACCOUNT3_BALANCE);
}