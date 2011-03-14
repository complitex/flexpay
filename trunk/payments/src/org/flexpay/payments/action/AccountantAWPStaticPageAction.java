package org.flexpay.payments.action;

import org.jetbrains.annotations.NotNull;

public class AccountantAWPStaticPageAction extends AccountantAWPActionSupport {

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		getUserPreferences().getCrumbs().removeAllElements();
		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}
}
