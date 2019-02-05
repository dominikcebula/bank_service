package com.dominikcebula.bank.service.rest.ds.request;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

@Setter
@Getter
public class TransferMoneyRequest {
    private AccountId from;
    private AccountId to;
    private Money amount;
}
