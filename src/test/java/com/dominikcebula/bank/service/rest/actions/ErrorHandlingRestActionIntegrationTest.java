package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.dto.ApiCode;
import com.dominikcebula.bank.service.dto.ApiErrorResponse;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.apache.http.Header;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dominikcebula.bank.service.assertions.HeadersAssert.assertHeadersContain;
import static com.dominikcebula.bank.service.rest.actions.ErrorHandlingRestAction.ERROR_MESSAGE;
import static com.dominikcebula.bank.service.rest.actions.ListAccountsRestAction.ACCOUNT_LIST_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class ErrorHandlingRestActionIntegrationTest extends SparkRestServerAwareTest {

    @Mock
    @Bind
    private ListAccountsRestAction listAccountsRestAction;

    @Test
    void shouldShowRestErrorOnNotFoundError() {
        ApiErrorResponse errorResponse = resetClient().getForObject("/non-existing-uri", ApiErrorResponse.class);

        assertEquals(ApiCode.FAILED, errorResponse.getStatus().getCode());
        assertEquals(ERROR_MESSAGE, errorResponse.getMessage());
    }

    @Test
    void shouldShowRestErrorOnInternalError() throws Exception {
        mockExceptionThrown();

        ApiErrorResponse errorResponse = resetClient().getForObject(ACCOUNT_LIST_URI, ApiErrorResponse.class);

        Mockito.verify(listAccountsRestAction).handle(Mockito.any(), Mockito.any());

        assertEquals(ApiCode.FAILED, errorResponse.getStatus().getCode());
        assertEquals(ERROR_MESSAGE, errorResponse.getMessage());
    }

    @Test
    void shouldIncludeJsonResponseTypeOnError() throws Exception {
        mockExceptionThrown();

        Header[] headers = resetClient().getForHeaders(ACCOUNT_LIST_URI);

        assertHeadersContain(headers, "Content-Type: application/json;charset=utf-8");
    }

    private void mockExceptionThrown() throws Exception {
        Mockito.when(listAccountsRestAction.handle(Mockito.any(), Mockito.any())).thenThrow(new IllegalArgumentException("TEST"));
    }
}