package org.flexpay.payments.actions.monitor;

import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;

public class PaymentCollectorMonitorPageAction extends AccountantAWPActionSupport {

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
