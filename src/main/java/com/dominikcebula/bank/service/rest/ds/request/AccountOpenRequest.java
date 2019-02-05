package com.dominikcebula.bank.service.rest.ds.request;

import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

@Setter
@Getter
public class AccountOpenRequest {

    private Money initialDeposit;
}
