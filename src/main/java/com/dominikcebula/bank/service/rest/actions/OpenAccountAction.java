package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.registry.AccountRegistry;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.rest.ds.request.AccountOpenRequest;
import com.dominikcebula.bank.service.rest.ds.response.AccountOpenResponse;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.AccountOpenRequestValidator;

public class OpenAccountAction extends AbstractValidatingRestAction<AccountOpenRequest, AccountOpenResponse> {

    private final AccountRegistry accountRegistry = AccountRegistry.getInstance();
    private final MoneyFactory moneyFactory = new MoneyFactory();

    public OpenAccountAction() {
        super(AccountOpenRequest.class, AccountOpenResponse.class);
    }

    @Override
    public Validator<AccountOpenRequest> getRequestValidator() {
        return new AccountOpenRequestValidator();
    }

    @Override
    public AccountOpenResponse handleRequest(AccountOpenRequest accountOpenRequest) throws Exception {
        return new AccountOpenResponse(
                accountRegistry.openAccount(
                        moneyFactory.create(accountOpenRequest.getInitialDeposit())
                )
        );
    }
}
