package org.flexpay.payments.actions.registry;

import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.jetbrains.annotations.NotNull;

public class RegistryDeliveryHistoryPageAction extends CashboxCookieActionSupport {

    @NotNull
	@Override
    protected String doExecute() throws Exception {
        return SUCCESS;
    }

    @NotNull
	@Override
    protected String getErrorResult() {
        return SUCCESS;
    }

}
