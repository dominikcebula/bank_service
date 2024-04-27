package com.dominikcebula.bank.service.application.utils;

import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.application.exception.AccountCreateException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Set;

import static com.dominikcebula.bank.service.application.utils.AccountIdGenerator.MESSAGE_GENERATION_ERROR;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AccountIdGeneratorTest {

    private static final String ACCOUNT_NUMBER_REGEXP = "^[0-9]{16}$";

    private final AccountIdGenerator accountIdGenerator = new AccountIdGenerator();

    @Mock
    private Set<AccountId> accountIds;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldCreateAccountId() {
        AccountId accountId = accountIdGenerator.generateAccountId(Collections.emptySet());

        assertThat(accountId.getAccountNumber())
                .matches(ACCOUNT_NUMBER_REGEXP);
    }

    @Test
    public void shouldThrowExceptionWhenUnableToGenerateAccountNumber() {
        Mockito.when(accountIds.contains(Mockito.any(AccountId.class))).thenReturn(true);

        expectedException.expect(AccountCreateException.class);
        expectedException.expectMessage(MESSAGE_GENERATION_ERROR);

        accountIdGenerator.generateAccountId(accountIds);
    }
}