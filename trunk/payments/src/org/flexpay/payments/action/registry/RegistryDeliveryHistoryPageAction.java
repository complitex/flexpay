package org.flexpay.payments.action.registry;

import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;

public class RegistryDeliveryHistoryPageAction extends AccountantAWPActionSupport {

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
