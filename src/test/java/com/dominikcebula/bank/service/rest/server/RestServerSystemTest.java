package com.dominikcebula.bank.service.rest.server;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.dto.*;
import com.dominikcebula.bank.service.rest.actions.OpenAccountRestAction;
import com.dominikcebula.bank.service.rest.actions.TransferMoneyRestAction;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static com.dominikcebula.bank.service.rest.actions.ListAccountsRestAction.ACCOUNT_LIST_URI;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestServerSystemTest extends SparkRestServerAwareTest {

    private MoneyFactory moneyFactory;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        moneyFactory = injector.getInstance(MoneyFactory.class);
    }

    @Test
    public void shouldExecuteScenarioCorrectly() {
        Accounts accountsBeforeScenario = getAccountsList();

        AccountId account1 = openAccount(moneyFactory.create(500));
        AccountId account2 = openAccount(moneyFactory.create(1000));
        AccountId account3 = openAccount(moneyFactory.create(1500));

        Accounts accountsBeforeTransfers = getAccountsList();

        transfer(account1, account2, moneyFactory.create(10));
        transfer(account2, account1, moneyFactory.create(10));
        transfer(account1, account3, moneyFactory.create(10));
        transfer(account3, account1, moneyFactory.create(10));
        transfer(account2, account3, moneyFactory.create(250));
        transfer(account3, account1, moneyFactory.create(1700));

        Accounts accountsAfterScenario = getAccountsList();

        assertStateBeforeScenario(accountsBeforeScenario);
        assertStateBeforeTransfersScenario(accountsBeforeTransfers, account1, account2, account3);
        assertStateAfterScenario(accountsAfterScenario, account1, account2, account3);
    }

    private void assertStateBeforeScenario(Accounts accountsBeforeScenario) {
        assertTrue(accountsBeforeScenario.getAccounts().isEmpty());
        assertEquals(accountsBeforeScenario.getTotalDeposit(), moneyFactory.create(0));
    }

    private void assertStateBeforeTransfersScenario(Accounts accountsBeforeTransfers, AccountId account1, AccountId account2, AccountId account3) {
        assertAccountsExists(accountsBeforeTransfers, account1, account2, account3);
        assertAccountDeposit(accountsBeforeTransfers, account1, moneyFactory.create(500));
        assertAccountDeposit(accountsBeforeTransfers, account2, moneyFactory.create(1000));
        assertAccountDeposit(accountsBeforeTransfers, account3, moneyFactory.create(1500));
        assertEquals(accountsBeforeTransfers.getTotalDeposit(), moneyFactory.create(3000));
    }

    private void assertStateAfterScenario(Accounts accountsAfterScenario, AccountId account1, AccountId account2, AccountId account3) {
        assertAccountsExists(accountsAfterScenario, account1, account2, account3);
        assertAccountDeposit(accountsAfterScenario, account1, moneyFactory.create(2200));
        assertAccountDeposit(accountsAfterScenario, account2, moneyFactory.create(750));
        assertAccountDeposit(accountsAfterScenario, account3, moneyFactory.create(50));
        assertEquals(accountsAfterScenario.getTotalDeposit(), moneyFactory.create(3000));
    }

    private Accounts getAccountsList() {
        return resetClient().getForObject(ACCOUNT_LIST_URI, Accounts.class);
    }

    private AccountId openAccount(Money initialDeposit) {
        AccountOpenRequest accountOpenRequest = new AccountOpenRequest();
        accountOpenRequest.setInitialDeposit(initialDeposit.getNumberStripped());

        AccountOpenResponse accountOpenResponse = resetClient().postForObject(OpenAccountRestAction.ACCOUNTS_OPEN_URI, accountOpenRequest,
                AccountOpenRequest.class, AccountOpenResponse.class);

        return AccountId.createAccountNumber(accountOpenResponse.getAccount().getAccountId());
    }

    private void transfer(AccountId from, AccountId to, Money amount) {
        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(from.getAccountNumber());
        transferMoneyRequest.setTo(to.getAccountNumber());
        transferMoneyRequest.setAmount(amount.getNumberStripped());

        TransferMoneyResponse transferMoneyResponse = resetClient().postForObject(
                TransferMoneyRestAction.TRANSFER_URI, transferMoneyRequest,
                TransferMoneyRequest.class, TransferMoneyResponse.class
        );

        assertEquals(ApiCode.MONEY_TRANSFERED, transferMoneyResponse.getStatus().getCode());
    }

    private void assertAccountsExists(Accounts accountsInfo, AccountId... accountIds) {
        assertThat(
                getAccountIdsSet(accountsInfo)
        ).containsOnly(accountIds);
    }

    private Set<AccountId> getAccountIdsSet(Accounts accountsInfo) {
        return accountsInfo.getAccounts().stream()
                .map(Account::getAccountId)
                .map(AccountId::createAccountNumber)
                .collect(Collectors.toSet());
    }

    private void assertAccountDeposit(Accounts accountsInfo, AccountId accountId, Money expectedDeposit) {
        Account accountInfo = findAccountById(accountsInfo, accountId);

        assertEquals(expectedDeposit.getNumberStripped(), accountInfo.getBalance());
    }

    private Account findAccountById(Accounts accountsInfo, AccountId accountId) {
        return accountsInfo.getAccounts().stream()
                .filter(a -> a.getAccountId().equals(accountId.getAccountNumber()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Unable to find account " + accountId));
    }
}