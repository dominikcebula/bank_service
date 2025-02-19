package com.dominikcebula.bank.service.application.utils;

import com.dominikcebula.bank.service.application.ds.AccountId;
import com.dominikcebula.bank.service.application.exception.AccountCreateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static com.dominikcebula.bank.service.application.utils.AccountIdGenerator.MESSAGE_GENERATION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AccountIdGeneratorTest {

    private static final String ACCOUNT_NUMBER_REGEXP = "^[0-9]{16}$";

    private final AccountIdGenerator accountIdGenerator = new AccountIdGenerator();

    @Mock
    private Set<AccountId> accountIds;

    @Test
    void shouldCreateAccountId() {
        AccountId accountId = accountIdGenerator.generateAccountId(Collections.emptySet());

        assertThat(accountId.getAccountNumber())
                .matches(ACCOUNT_NUMBER_REGEXP);
    }

    @Test
    void shouldThrowExceptionWhenUnableToGenerateAccountNumber() {
        Mockito.when(accountIds.contains(Mockito.any(AccountId.class))).thenReturn(true);

        AccountCreateException thrownException = assertThrows(AccountCreateException.class,
                () -> accountIdGenerator.generateAccountId(accountIds)
        );

        assertThat(thrownException.getMessage())
                .isEqualTo(MESSAGE_GENERATION_ERROR);
    }
}