package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.application.actions.BankActionsFacade;
import com.dominikcebula.bank.service.dto.Account;
import com.dominikcebula.bank.service.dto.Accounts;
import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ListAccountsResponse;
import com.dominikcebula.bank.service.rest.actions.base.NoActionsFacadeInContextIntegrationTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.dominikcebula.bank.service.rest.actions.ListAccountsRestAction.ACCOUNT_LIST_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class ListAccountsRestActionIntegrationTest extends NoActionsFacadeInContextIntegrationTest {

    private static final int TOTAL_DEPOSIT = 600;

    @Mock
    @Bind
    private BankActionsFacade bankActionsFacade;

    @Test
    void shouldListCreatedAccounts() {
        Mockito.when(bankActionsFacade.listAccounts()).thenReturn(getAccounts());

        ListAccountsResponse listAccountsResponse = resetClient().getForObject(ACCOUNT_LIST_URI, ListAccountsResponse.class);

        assertThat(listAccountsResponse.getAccounts().getAccounts())
                .containsOnly(getAccountsList());
        assertEquals(listAccountsResponse.getAccounts().getTotalDeposit().intValue(), TOTAL_DEPOSIT);
        assertEquals(listAccountsResponse.getStatus().getCode(), ApiCode.ACCOUNTS_LISTED);
    }

    private Accounts getAccounts() {
        return new Accounts()
                .accounts(Arrays.asList(getAccountsList()))
                .totalDeposit(BigDecimal.valueOf(600));
    }

    private Account[] getAccountsList() {
        return new Account[]{
                new Account().accountId("1").balance(BigDecimal.valueOf(100)),
                new Account().accountId("2").balance(BigDecimal.valueOf(200)),
                new Account().accountId("3").balance(BigDecimal.valueOf(300)),
        };
    }
}