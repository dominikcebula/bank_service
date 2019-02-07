package com.dominikcebula.bank.service.bls.actions;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.bls.exception.TransferException;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.guice.ContextAwareTest;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class BankActionsFacadeMultiThreadTest extends ContextAwareTest {

    private static final int NUMBER_OF_ACCOUNTS = 10000;
    private static final int NUMBER_OF_RANDOM_ACCOUNTS = 10000;
    private static final int NUMBER_OF_TRANSFERS = 10000;
    private static final int DEFAULT_DEPOSIT = 148;
    private static final int TOTAL_DEPOSIT = NUMBER_OF_ACCOUNTS * DEFAULT_DEPOSIT;

    private BankActionsFacadeInvoker bankActionsFacadeInvoker;
    private MoneyFactory moneyFactory;

    @Before
    public void setUp() {
        super.setUp();
        bankActionsFacadeInvoker = injector.getInstance(BankActionsFacadeInvoker.class);
        moneyFactory = injector.getInstance(MoneyFactory.class);
    }

    @Test
    public void shouldOpenAccountsCorrectly() {
        openDefaultAccounts();

        AccountsInfo accountsInfo = bankActionsFacadeInvoker.listAccounts();

        assertEquals(NUMBER_OF_ACCOUNTS, accountsInfo.getAccountsInfo().size());
        assertEquals(TOTAL_DEPOSIT, accountsInfo.getTotalDeposit().getNumber().intValue());
    }

    @Test
    public void shouldTransferMoneyBetweenAccountsCorrectly() throws TransferException {
        List<AccountId> sourceAccounts = openRandomAccounts();
        List<AccountId> destinationAccounts = openRandomAccounts();

        AccountsInfo accountsBeforeTransfer = bankActionsFacadeInvoker.listAccounts();

        performRandomTransfers(sourceAccounts, destinationAccounts);

        AccountsInfo accountsAfterTransfer = bankActionsFacadeInvoker.listAccounts();

        assertEquals(accountsBeforeTransfer.getAccountsInfo().size(), accountsAfterTransfer.getAccountsInfo().size());
        assertEquals(accountsBeforeTransfer.getTotalDeposit(), accountsAfterTransfer.getTotalDeposit());
    }

    private void openDefaultAccounts() {
        Collections.nCopies(NUMBER_OF_ACCOUNTS, moneyFactory.create(DEFAULT_DEPOSIT))
                .parallelStream()
                .forEach(this::openAccount);
    }

    private List<AccountId> openRandomAccounts() {
        return IntStream.generate(this::randomDeposit)
                .parallel()
                .limit(NUMBER_OF_RANDOM_ACCOUNTS)
                .mapToObj(moneyFactory::create)
                .map(this::openAccount)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private AccountId openAccount(Money amount) {
        return bankActionsFacadeInvoker.openAccount(amount);
    }

    private void performRandomTransfers(List<AccountId> sourceAccounts, List<AccountId> destinationAccounts) throws TransferException {
        IntStream.range(0, NUMBER_OF_TRANSFERS)
                .parallel()
                .forEach(i -> performTransfer(sourceAccounts, destinationAccounts));
    }

    @SneakyThrows
    private void performTransfer(List<AccountId> sourceAccounts, List<AccountId> destinationAccounts) {
        AccountId sourceAccount = getRandomAccount(sourceAccounts);
        AccountId destinationAccount = getRandomAccount(destinationAccounts);

        bankActionsFacadeInvoker.transfer(sourceAccount, destinationAccount, moneyFactory.create(randomTransfer()));
    }

    private AccountId getRandomAccount(List<AccountId> accounts) {
        return accounts.get(RandomUtils.nextInt(0, accounts.size()));
    }

    private int randomDeposit() {
        return randomMoney(3000000, 6000000);
    }

    private int randomTransfer() {
        return randomMoney(1, 10000);
    }

    private int randomMoney(int from, int to) {
        return RandomUtils.nextInt(from, to);
    }
}