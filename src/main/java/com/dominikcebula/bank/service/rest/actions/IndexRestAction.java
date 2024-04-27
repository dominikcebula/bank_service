package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.dto.HealthCheckResponse;
import com.dominikcebula.bank.service.dto.HealthCheckResponse.StatusEnum;
import com.dominikcebula.bank.service.rest.actions.base.AbstractRestAction;
import com.google.inject.Inject;

public class IndexRestAction extends AbstractRestAction<Void, HealthCheckResponse> {

    public static final String INDEX_ACTION_URI = "/";

    @Inject
    @SuppressWarnings("unused")
    public IndexRestAction() {
        super(Void.class, HealthCheckResponse.class);
    }

    @Override
    protected HealthCheckResponse handleRequest(Void request) {
        return new HealthCheckResponse()
                .status(StatusEnum.UP);
    }
}
