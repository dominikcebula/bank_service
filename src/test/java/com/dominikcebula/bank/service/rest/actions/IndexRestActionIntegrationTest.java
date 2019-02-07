package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import org.apache.http.Header;
import org.junit.Test;

import static com.dominikcebula.bank.service.assertions.HeadersAssert.assertHeadersContain;
import static com.dominikcebula.bank.service.rest.actions.IndexRestAction.INDEX_ACTION_URI;
import static com.dominikcebula.bank.service.rest.actions.IndexRestAction.MESSAGE_SERVICE_UP;
import static org.junit.Assert.assertEquals;

public class IndexRestActionIntegrationTest extends SparkRestServerAwareTest {

    @Test
    public void shouldIncludeDefaultResponse() {
        String indexResponse = resetClient().getForObject(INDEX_ACTION_URI, String.class);

        assertEquals(MESSAGE_SERVICE_UP, indexResponse);
    }

    @Test
    public void shouldIncludeJsonResponseType() {
        Header[] headers = resetClient().getForHeaders(INDEX_ACTION_URI);

        assertHeadersContain(headers, "Content-Type: application/json;charset=utf-8");
    }
}