package com.dominikcebula.bank.service.rest.server;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.dto.*;
import com.dominikcebula.bank.service.rest.actions.CreateAccountRestAction;
import com.dominikcebula.bank.service.rest.actions.TransferMoneyRestAction;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dominikcebula.bank.service.rest.actions.ListAccountsRestAction.ACCOUNT_LIST_URI;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestServerSystemTest extends SparkRestServerAwareTest {

    @Test
    public void shouldExecuteScenarioCorrectly() {
        Accounts accountsBeforeScenario = getAccountsList().getAccounts();

        AccountId account1 = openAccount(BigDecimal.valueOf(500));
        AccountId account2 = openAccount(BigDecimal.valueOf(1000));
        AccountId account3 = openAccount(BigDecimal.valueOf(1500));

        Accounts accountsBeforeTransfers = getAccountsList().getAccounts();

        transfer(account1, account2, BigDecimal.valueOf(10));
        transfer(account2, account1, BigDecimal.valueOf(10));
        transfer(account1, account3, BigDecimal.valueOf(10));
        transfer(account3, account1, BigDecimal.valueOf(10));
        transfer(account2, account3, BigDecimal.valueOf(250));
        transfer(account3, account1, BigDecimal.valueOf(1700));

        Accounts accountsAfterScenario = getAccountsList().getAccounts();

        assertStateBeforeScenario(accountsBeforeScenario);
        assertStateBeforeTransfersScenario(accountsBeforeTransfers, account1, account2, account3);
        assertStateAfterScenario(accountsAfterScenario, account1, account2, account3);
    }

    private void assertStateBeforeScenario(Accounts accountsBeforeScenario) {
        assertTrue(accountsBeforeScenario.getAccounts().isEmpty());
        assertEquals(accountsBeforeScenario.getTotalDeposit(), BigDecimal.valueOf(0));
    }

    private void assertStateBeforeTransfersScenario(Accounts accountsBeforeTransfers, AccountId account1, AccountId account2, AccountId account3) {
        assertAccountsExists(accountsBeforeTransfers, account1, account2, account3);
        assertAccountDeposit(accountsBeforeTransfers, account1, BigDecimal.valueOf(500));
        assertAccountDeposit(accountsBeforeTransfers, account2, BigDecimal.valueOf(1000));
        assertAccountDeposit(accountsBeforeTransfers, account3, BigDecimal.valueOf(1500));
        assertEquals(accountsBeforeTransfers.getTotalDeposit(), BigDecimal.valueOf(3000));
    }

    private void assertStateAfterScenario(Accounts accountsAfterScenario, AccountId account1, AccountId account2, AccountId account3) {
        assertAccountsExists(accountsAfterScenario, account1, account2, account3);
        assertAccountDeposit(accountsAfterScenario, account1, BigDecimal.valueOf(2200));
        assertAccountDeposit(accountsAfterScenario, account2, BigDecimal.valueOf(750));
        assertAccountDeposit(accountsAfterScenario, account3, BigDecimal.valueOf(50));
        assertEquals(accountsAfterScenario.getTotalDeposit(), BigDecimal.valueOf(3000));
    }

    private ListAccountsResponse getAccountsList() {
        return resetClient().getForObject(ACCOUNT_LIST_URI, ListAccountsResponse.class);
    }

    private AccountId openAccount(BigDecimal initialDeposit) {
        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setInitialDeposit(initialDeposit);

        AccountCreateResponse accountCreateResponse = resetClient().postForObject(CreateAccountRestAction.ACCOUNTS_CREATE_URI, accountCreateRequest,
                AccountCreateRequest.class, AccountCreateResponse.class);

        return AccountId.createAccountNumber(accountCreateResponse.getAccount().getAccountId());
    }

    private void transfer(AccountId from, AccountId to, BigDecimal amount) {
        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setFrom(from.getAccountNumber());
        transferMoneyRequest.setTo(to.getAccountNumber());
        transferMoneyRequest.setAmount(amount);

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

    private void assertAccountDeposit(Accounts accountsInfo, AccountId accountId, BigDecimal expectedDeposit) {
        Account accountInfo = findAccountById(accountsInfo, accountId);

        assertEquals(expectedDeposit, accountInfo.getBalance());
    }

    private Account findAccountById(Accounts accountsInfo, AccountId accountId) {
        return accountsInfo.getAccounts().stream()
                .filter(a -> a.getAccountId().equals(accountId.getAccountNumber()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Unable to find account " + accountId));
    }
}