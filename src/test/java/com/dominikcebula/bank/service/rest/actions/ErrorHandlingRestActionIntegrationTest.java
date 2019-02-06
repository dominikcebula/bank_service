package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.rest.ds.response.ErrorResponse;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import com.google.inject.testing.fieldbinder.Bind;
import org.apache.http.Header;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static com.dominikcebula.bank.service.assertions.HeadersAssert.assertHeadersContain;
import static com.dominikcebula.bank.service.rest.actions.ErrorHandlingRestAction.ERROR_MESSAGE;
import static com.dominikcebula.bank.service.rest.actions.ListAccountsRestAction.ACCOUNT_LIST_URI;
import static com.dominikcebula.bank.service.rest.ds.response.Response.Status;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ErrorHandlingRestActionIntegrationTest extends SparkRestServerAwareTest {

    @Mock
    @Bind
    private ListAccountsRestAction listAccountsRestAction;

    @Test
    public void shouldShowRestErrorOnNotFoundError() {
        ErrorResponse errorResponse = resetClient().getForObject("/non-existing-uri", ErrorResponse.class);

        assertEquals(Status.ERROR, errorResponse.getStatus());
        assertEquals(ERROR_MESSAGE, errorResponse.getMessage());
    }

    @Test
    public void shouldShowRestErrorOnInternalError() throws Exception {
        mockExceptionThrown();

        ErrorResponse errorResponse = resetClient().getForObject(ACCOUNT_LIST_URI, ErrorResponse.class);

        Mockito.verify(listAccountsRestAction).handle(Mockito.any(), Mockito.any());

        assertEquals(Status.ERROR, errorResponse.getStatus());
        assertEquals(ERROR_MESSAGE, errorResponse.getMessage());
    }

    @Test
    public void shouldIncludeJsonResponseTypeOnError() throws Exception {
        mockExceptionThrown();

        Header[] headers = resetClient().getForHeaders(ACCOUNT_LIST_URI);

        assertHeadersContain(headers, "Content-Type: application/json;charset=utf-8");
    }

    private void mockExceptionThrown() throws Exception {
        Mockito.when(listAccountsRestAction.handle(Mockito.any(), Mockito.any())).thenThrow(new IllegalArgumentException("TEST"));
    }
}