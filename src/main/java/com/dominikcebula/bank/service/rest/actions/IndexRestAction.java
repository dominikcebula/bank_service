package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.dto.HealthCheckResponse;
import com.dominikcebula.bank.service.dto.HealthCheckResponse.StatusEnum;
import com.dominikcebula.bank.service.rest.json.GsonProvider;
import com.google.inject.Inject;

public class IndexRestAction extends AbstractRestAction<Void, HealthCheckResponse> {

    public static final String INDEX_ACTION_URI = "/";

    @Inject
    public IndexRestAction(GsonProvider gsonProvider) {
        super(gsonProvider, Void.class, HealthCheckResponse.class);
    }

    @Override
    HealthCheckResponse handleRequest(Void request) {
        HealthCheckResponse healthCheckResponse = new HealthCheckResponse();
        healthCheckResponse.setStatus(StatusEnum.UP);
        return healthCheckResponse;
    }
}
