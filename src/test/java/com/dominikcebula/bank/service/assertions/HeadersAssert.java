package com.dominikcebula.bank.service.assertions;

import org.apache.http.Header;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.fest.assertions.api.Assertions.assertThat;

public class HeadersAssert {
    public static void assertHeadersContain(Header[] headers, String... expectedHeaders) {
        Set<String> headersEntries = Arrays.stream(headers)
                .map(HeadersAssert::getHeaderEntryAsString)
                .collect(Collectors.toSet());

        assertThat(headersEntries)
                .contains(expectedHeaders);
    }

    private static String getHeaderEntryAsString(Header h) {
        return String.format("%s: %s", h.getName(), h.getValue());
    }
}
