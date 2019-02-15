package com.dominikcebula.bank.service.rest.server;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.bls.ds.AccountInfo;
import com.dominikcebula.bank.service.bls.ds.AccountsInfo;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.dto.AccountOpenRequest;
import com.dominikcebula.bank.service.dto.AccountOpenResponse;
import com.dominikcebula.bank.service.dto.TransferMoneyRequest;
import com.dominikcebula.bank.service.dto.TransferMoneyResponse;
import com.dominikcebula.bank.service.rest.actions.OpenAccountRestAction;
import com.dominikcebula.bank.service.rest.actions.TransferMoneyRestAction;
import com.dominikcebula.bank.service.rest.ds.response.Response;
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
        AccountsInfo accountsBeforeScenario = getAccountsList();

        AccountId account1 = openAccount(moneyFactory.create(500));
        AccountId account2 = openAccount(moneyFactory.create(1000));
        AccountId account3 = openAccount(moneyFactory.create(1500));

        AccountsInfo accountsBeforeTransfers = getAccountsList();

        transfer(account1, account2, moneyFactory.create(10));
        transfer(account2, account1, moneyFactory.create(10));
        transfer(account1, account3, moneyFactory.create(10));
        transfer(account3, account1, moneyFactory.create(10));
        transfer(account2, account3, moneyFactory.create(250));
        transfer(account3, account1, moneyFactory.create(1700));

        AccountsInfo accountsAfterScenario = getAccountsList();

        assertStateBeforeScenario(accountsBeforeScenario);
        assertStateBeforeTransfersScenario(accountsBeforeTransfers, account1, account2, account3);
        assertStateAfterScenario(accountsAfterScenario, account1, account2, account3);
    }

    private void assertStateBeforeScenario(AccountsInfo accountsBeforeScenario) {
        assertTrue(accountsBeforeScenario.getAccountsInfo().isEmpty());
        assertEquals(accountsBeforeScenario.getTotalDeposit(), moneyFactory.create(0));
    }

    private void assertStateBeforeTransfersScenario(AccountsInfo accountsBeforeTransfers, AccountId account1, AccountId account2, AccountId account3) {
        assertAccountsExists(accountsBeforeTransfers, account1, account2, account3);
        assertAccountDeposit(accountsBeforeTransfers, account1, moneyFactory.create(500));
        assertAccountDeposit(accountsBeforeTransfers, account2, moneyFactory.create(1000));
        assertAccountDeposit(accountsBeforeTransfers, account3, moneyFactory.create(1500));
        assertEquals(accountsBeforeTransfers.getTotalDeposit(), moneyFactory.create(3000));
    }

    private void assertStateAfterScenario(AccountsInfo accountsAfterScenario, AccountId account1, AccountId account2, AccountId account3) {
        assertAccountsExists(accountsAfterScenario, account1, account2, account3);
        assertAccountDeposit(accountsAfterScenario, account1, moneyFactory.create(2200));
        assertAccountDeposit(accountsAfterScenario, account2, moneyFactory.create(750));
        assertAccountDeposit(accountsAfterScenario, account3, moneyFactory.create(50));
        assertEquals(accountsAfterScenario.getTotalDeposit(), moneyFactory.create(3000));
    }

    private AccountsInfo getAccountsList() {
        return resetClient().getForObject(ACCOUNT_LIST_URI, AccountsInfo.class);
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

        assertEquals(Response.Status.SUCCESS, transferMoneyResponse.getStatus());
    }

    private void assertAccountsExists(AccountsInfo accountsInfo, AccountId... accountIds) {
        assertThat(
                getAccountIdsSet(accountsInfo)
        ).containsOnly(accountIds);
    }

    private Set<AccountId> getAccountIdsSet(AccountsInfo accountsInfo) {
        return accountsInfo.getAccountsInfo().stream()
                .map(AccountInfo::getAccountId)
                .collect(Collectors.toSet());
    }

    private void assertAccountDeposit(AccountsInfo accountsInfo, AccountId accountId, Money expectedDeposit) {
        AccountInfo accountInfo = findAccountById(accountsInfo, accountId);

        assertEquals(expectedDeposit, accountInfo.getBalance());
    }

    private AccountInfo findAccountById(AccountsInfo accountsInfo, AccountId accountId) {
        return accountsInfo.getAccountsInfo().stream()
                .filter(a -> a.getAccountId().equals(accountId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Unable to find account " + accountId));
    }
}