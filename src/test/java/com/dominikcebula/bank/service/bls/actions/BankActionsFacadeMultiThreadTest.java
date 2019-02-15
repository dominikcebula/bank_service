package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;
import com.dominikcebula.bank.service.guice.ContextAwareTest;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class BankActionsFacadeMultiThreadTest extends ContextAwareTest {

    private static final int NUMBER_OF_ACCOUNTS = 10000;
    private static final int NUMBER_OF_RANDOM_ACCOUNTS = 10000;
    private static final int NUMBER_OF_TRANSFERS = 10000;
    private static final int DEFAULT_DEPOSIT = 148;
    private static final int TOTAL_DEPOSIT = NUMBER_OF_ACCOUNTS * DEFAULT_DEPOSIT;

    private BankActionsFacadeInvoker bankActionsFacadeInvoker;

    @Before
    public void setUp() {
        super.setUp();
        bankActionsFacadeInvoker = injector.getInstance(BankActionsFacadeInvoker.class);
    }

    @Test
    public void shouldOpenAccountsCorrectly() {
        openDefaultAccounts();

        Accounts accounts = bankActionsFacadeInvoker.listAccounts();

        assertEquals(NUMBER_OF_ACCOUNTS, accounts.getAccounts().size());
        assertEquals(TOTAL_DEPOSIT, accounts.getTotalDeposit().intValue());
    }

    @Test
    public void shouldTransferMoneyBetweenAccountsCorrectly() throws TransferException {
        List<Account> sourceAccounts = openRandomAccounts();
        List<Account> destinationAccounts = openRandomAccounts();

        Accounts accountsBeforeTransfer = bankActionsFacadeInvoker.listAccounts();

        performRandomTransfers(sourceAccounts, destinationAccounts);

        Accounts accountsAfterTransfer = bankActionsFacadeInvoker.listAccounts();

        assertEquals(accountsBeforeTransfer.getAccounts().size(), accountsAfterTransfer.getAccounts().size());
        assertEquals(accountsBeforeTransfer.getTotalDeposit(), accountsAfterTransfer.getTotalDeposit());
    }

    private void openDefaultAccounts() {
        Collections.nCopies(NUMBER_OF_ACCOUNTS, BigDecimal.valueOf(DEFAULT_DEPOSIT))
                .parallelStream()
                .forEach(this::openAccount);
    }

    private List<Account> openRandomAccounts() {
        return Stream.generate(this::randomDeposit)
                .parallel()
                .limit(NUMBER_OF_RANDOM_ACCOUNTS)
                .map(this::openAccount)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private Account openAccount(BigDecimal amount) {
        return bankActionsFacadeInvoker.openAccount(amount);
    }

    private void performRandomTransfers(List<Account> sourceAccounts, List<Account> destinationAccounts) throws TransferException {
        IntStream.range(0, NUMBER_OF_TRANSFERS)
                .parallel()
                .forEach(i -> performTransfer(sourceAccounts, destinationAccounts));
    }

    @SneakyThrows
    private void performTransfer(List<Account> sourceAccounts, List<Account> destinationAccounts) {
        Account sourceAccount = getRandomAccount(sourceAccounts);
        Account destinationAccount = getRandomAccount(destinationAccounts);

        bankActionsFacadeInvoker.transfer(
                AccountId.createAccountNumber(sourceAccount.getAccountId()),
                AccountId.createAccountNumber(destinationAccount.getAccountId()),
                randomTransfer()
        );
    }

    private Account getRandomAccount(List<Account> accounts) {
        return accounts.get(RandomUtils.nextInt(0, accounts.size()));
    }

    private BigDecimal randomDeposit() {
        return randomMoney(3000000, 6000000);
    }

    private BigDecimal randomTransfer() {
        return randomMoney(1, 10000);
    }

    private BigDecimal randomMoney(int from, int to) {
        return BigDecimal.valueOf(RandomUtils.nextInt(from, to));
    }
}