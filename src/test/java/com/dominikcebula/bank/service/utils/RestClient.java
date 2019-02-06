package com.dominikcebula.bank.service.utils;

import com.dominikcebula.bank.service.configuration.Configuration;
import com.google.gson.Gson;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStreamReader;

public class RestClient {

    private final Configuration configuration;
    private final Gson gson;

    public RestClient(Configuration configuration, Gson gson) {
        this.configuration = configuration;
        this.gson = gson;
    }

    public <T> T getForObject(String uri, Class<T> responseClass) {
        HttpGet httpGet = new HttpGet(uri);

        return executeForEntity(httpGet, responseClass);
    }

    public Header[] getForHeaders(String uri) {
        HttpGet httpGet = new HttpGet(uri);

        return executeForResponse(httpGet).getAllHeaders();
    }

    private <T> T executeForEntity(HttpRequest httpRequest, Class<T> responseClass) {
        try (
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse httpResponse = httpClient.execute(getTarget(), httpRequest);
                InputStreamReader contentStream = new InputStreamReader(httpResponse.getEntity().getContent());
        ) {
            return gson.fromJson(contentStream, responseClass);
        } catch (Exception e) {
            throw new RestClientException(e.getMessage(), e);
        }
    }

    private CloseableHttpResponse executeForResponse(HttpRequest httpRequest) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            return httpClient.execute(getTarget(), httpRequest);
        } catch (Exception e) {
            throw new RestClientException(e.getMessage(), e);
        }
    }

    private HttpHost getTarget() {
        return HttpHost.create(
                String.format("http://%s:%d", configuration.getHost(), configuration.getPort())
        );
    }

    private static class RestClientException extends RuntimeException {
        private RestClientException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
