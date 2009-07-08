package org.flexpay.payments.actions.reports;

import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.jetbrains.annotations.NotNull;

public class AccPaymentsReportAction extends CashboxCookieActionSupport {

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		// TODO implement

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {

		return SUCCESS;
	}
}
