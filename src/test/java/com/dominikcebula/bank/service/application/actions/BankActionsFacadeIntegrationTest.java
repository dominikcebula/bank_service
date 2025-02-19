package com.dominikcebula.bank.service.application.actions;

import com.dominikcebula.bank.service.application.dao.AccountDao;
import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.application.exception.TransferException;
import com.dominikcebula.bank.service.application.utils.AccountIdGenerator;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;
import com.dominikcebula.bank.service.guice.ContextAwareTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class BankActionsFacadeIntegrationTest extends ContextAwareTest {

    private static final AccountId ACCOUNT_ID_1 = AccountId.createAccountNumber("1");
    private static final AccountId ACCOUNT_ID_2 = AccountId.createAccountNumber("2");
    private static final AccountId ACCOUNT_ID_3 = AccountId.createAccountNumber("3");
    private static final AccountId NON_EXISTING_ACCOUNT = AccountId.createAccountNumber("4");
    private static final BigDecimal BALANCE1 = BigDecimal.valueOf(500.25);
    private static final BigDecimal HUGE_AMOUNT = BALANCE1.multiply(BigDecimal.valueOf(10.00));
    private static final BigDecimal BALANCE2 = BigDecimal.valueOf(600.36);
    private static final BigDecimal BALANCE3 = BigDecimal.valueOf(700.47);
    private static final BigDecimal TOTAL_BALANCE = BALANCE1.add(BALANCE2).add(BALANCE3);
    private static final BigDecimal NO_MONEY = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
    private static final BigDecimal FLOAT_AMOUNT = BigDecimal.valueOf(5.671);
    private static final BigDecimal FLOAT_AMOUNT_ROUNDED = BigDecimal.valueOf(5.67);

    private BankActionsFacade bankActionsFacade;
    private AccountDao accountDao;
    @Mock
    @Bind
    private AccountIdGenerator accountIdGenerator;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        bankActionsFacade = injector.getInstance(BankActionsFacade.class);
        accountDao = injector.getInstance(AccountDao.class);
    }

    @Test
    void shouldCreateAccounts() throws InterruptedException {
        createAccounts();

        assertThat(accountDao.findAccountIdentifiers())
                .containsOnly(ACCOUNT_ID_1, ACCOUNT_ID_2, ACCOUNT_ID_3);
    }

    @Test
    void shouldListAccounts() throws InterruptedException {
        createAccounts();

        Accounts accounts = bankActionsFacade.listAccounts();

        assertThat(accounts.getAccounts())
                .containsOnly(
                        new Account().accountId(ACCOUNT_ID_1.getAccountNumber()).balance(BALANCE1),
                        new Account().accountId(ACCOUNT_ID_2.getAccountNumber()).balance(BALANCE2),
                        new Account().accountId(ACCOUNT_ID_3.getAccountNumber()).balance(BALANCE3)
                );
        assertEquals(TOTAL_BALANCE, accounts.getTotalDeposit());
    }

    @Test
    void shouldListAccountsWhenNoneCreated() {

        Accounts accounts = bankActionsFacade.listAccounts();

        assertTrue(accounts.getAccounts().isEmpty());
        assertEquals(NO_MONEY, accounts.getTotalDeposit());
    }

    @Test
    void shouldTransferMoneyBetweenAccounts() throws InterruptedException {
        createAccounts();

        bankActionsFacade.transfer(ACCOUNT_ID_1, ACCOUNT_ID_2, BALANCE1);

        assertEquals(NO_MONEY, accountDao.findAccount(ACCOUNT_ID_1).getBalance());
        assertEquals(BALANCE1.add(BALANCE2), accountDao.findAccount(ACCOUNT_ID_2).getBalance());
        assertEquals(BALANCE3, accountDao.findAccount(ACCOUNT_ID_3).getBalance());
    }

    @Test
    void shouldTransferRoundedMoneyBetweenAccounts() throws InterruptedException {
        createAccounts();

        bankActionsFacade.transfer(ACCOUNT_ID_1, ACCOUNT_ID_2, FLOAT_AMOUNT);

        assertEquals(BALANCE2.add(FLOAT_AMOUNT_ROUNDED).floatValue(), accountDao.findAccount(ACCOUNT_ID_2).getBalance().floatValue(), 0f);
    }

    @Test
    void shouldFailTransferBecauseOfMissingAccount() throws InterruptedException {
        createAccounts();

        TransferException thrownException = assertThrows(TransferException.class, () -> {
            bankActionsFacade.transfer(NON_EXISTING_ACCOUNT, ACCOUNT_ID_2, BALANCE1);
        });

        assertThat(thrownException.getMessage())
                .contains("Unable to locate account [" + NON_EXISTING_ACCOUNT + "]");
    }

    @Test
    void shouldFailTransferBecauseOfNoEnoughFunds() throws InterruptedException {
        createAccounts();

        TransferException thrownException = assertThrows(TransferException.class, () -> {
            bankActionsFacade.transfer(ACCOUNT_ID_1, ACCOUNT_ID_2, HUGE_AMOUNT);
        });

        assertThat(thrownException.getMessage())
                .contains("Unable to transfer amount [" + HUGE_AMOUNT + "] from [" + ACCOUNT_ID_1 + "] to [" + ACCOUNT_ID_2 + "]");
    }

    private void createAccounts() throws InterruptedException {
        mockAccountId(ACCOUNT_ID_1);
        bankActionsFacade.createAccount(BALANCE1);

        mockAccountId(ACCOUNT_ID_2);
        bankActionsFacade.createAccount(BALANCE2);

        mockAccountId(ACCOUNT_ID_3);
        bankActionsFacade.createAccount(BALANCE3);
    }

    private void mockAccountId(AccountId accountId) {
        Mockito.when(accountIdGenerator.generateAccountId(Mockito.anySet()))
                .thenReturn(accountId);
    }
}