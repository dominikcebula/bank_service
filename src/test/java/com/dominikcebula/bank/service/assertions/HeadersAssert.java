package com.dominikcebula.bank.service.assertions;

import org.apache.http.Header;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.fest.assertions.api.Assertions.assertThat;

public class HeadersAssert {
    public static void assertHeadersContain(Header[] headers, String... expectedHeaders) {
        assertThat(getHeaderEntriesSet(headers))
                .contains(expectedHeaders);
    }

    private static Set<String> getHeaderEntriesSet(Header[] headers) {
        return Arrays.stream(headers)
                .map(HeadersAssert::getHeaderEntryAsString)
                .collect(Collectors.toSet());
    }

    private static String getHeaderEntryAsString(Header h) {
        return String.format("%s: %s", h.getName(), h.getValue());
    }
}
