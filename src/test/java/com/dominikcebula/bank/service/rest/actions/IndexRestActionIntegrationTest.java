package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.dto.HealthCheckResponse;
import com.dominikcebula.bank.service.dto.HealthCheckResponse.StatusEnum;
import com.dominikcebula.bank.service.spark.SparkRestServerAwareTest;
import org.apache.http.Header;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.dominikcebula.bank.service.assertions.HeadersAssert.assertHeadersContain;
import static com.dominikcebula.bank.service.rest.actions.IndexRestAction.INDEX_ACTION_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;


class IndexRestActionIntegrationTest extends SparkRestServerAwareTest {

    @Test
    void shouldIncludeDefaultResponse() {
        HealthCheckResponse indexResponse = resetClient().getForObject(INDEX_ACTION_URI, HealthCheckResponse.class);

        assertEquals(StatusEnum.UP, indexResponse.getStatus());
    }

    @Test
    void shouldIncludeJsonResponseType() throws IOException {
        Header[] headers = resetClient().getForHeaders(INDEX_ACTION_URI);

        assertHeadersContain(headers, "Content-Type: application/json;charset=utf-8");
    }
}