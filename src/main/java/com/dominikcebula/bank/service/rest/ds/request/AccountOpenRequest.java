package com.dominikcebula.bank.service.rest.ds.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountOpenRequest {
    private float initialDeposit;
    private String currency;
}
