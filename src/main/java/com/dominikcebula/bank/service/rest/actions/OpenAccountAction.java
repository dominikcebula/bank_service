package com.dominikcebula.bank.service.rest.actions;

import com.dominikcebula.bank.service.bls.actions.ActionsFacade;
import com.dominikcebula.bank.service.bls.utils.MoneyFactory;
import com.dominikcebula.bank.service.configuration.Configuration;
import com.dominikcebula.bank.service.rest.ds.request.AccountOpenRequest;
import com.dominikcebula.bank.service.rest.ds.response.AccountOpenResponse;
import com.dominikcebula.bank.service.rest.validator.Validator;
import com.dominikcebula.bank.service.rest.validator.validators.AccountOpenRequestValidator;

public class OpenAccountAction extends AbstractValidatingRestAction<AccountOpenRequest, AccountOpenResponse> {

    private final ActionsFacade actionsFacade;
    private final MoneyFactory moneyFactory;
    private final Configuration configuration;

    public OpenAccountAction(ActionsFacade actionsFacade, MoneyFactory moneyFactory, Configuration configuration) {
        super(AccountOpenRequest.class, AccountOpenResponse.class);
        this.actionsFacade = actionsFacade;
        this.moneyFactory = moneyFactory;
        this.configuration = configuration;
    }

    @Override
    public Validator<AccountOpenRequest> getRequestValidator() {
        return new AccountOpenRequestValidator(configuration);
    }

    @Override
    public AccountOpenResponse handleRequest(AccountOpenRequest accountOpenRequest) throws Exception {
        return new AccountOpenResponse(
                actionsFacade.openAccount(
                        moneyFactory.create(accountOpenRequest.getInitialDeposit())
                )
        );
    }
}
