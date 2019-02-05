package com.dominikcebula.bank.service.rest.ds.response;

import com.dominikcebula.bank.service.bls.ds.AccountId;
import com.dominikcebula.bank.service.rest.ds.request.TransferMoneyRequest;
import lombok.Getter;
import org.javamoney.moneta.Money;

@Getter
public class TransferMoneyResponse extends Response {

    private final AccountId from;
    private final AccountId to;
    private final Money amount;

    public TransferMoneyResponse(TransferMoneyRequest transferMoneyRequest) {
        super(Status.SUCCESS,
                String.format(
                        "Transferred [%s] from [%s] to [%s]",
                        transferMoneyRequest.getAmount(), transferMoneyRequest.getFrom(), transferMoneyRequest.getTo()
                )
        );

        from = transferMoneyRequest.getFrom();
        to = transferMoneyRequest.getTo();
        amount = transferMoneyRequest.getAmount();
    }
}
