package org.flexpay.payments.actions.search;

import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class SearchByQuittanceNumberAction extends FPActionSupport {

	@NotNull
	protected String doExecute() throws Exception {

		// TODO implement

		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}
}
