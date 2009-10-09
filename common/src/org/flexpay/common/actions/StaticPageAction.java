package org.flexpay.common.actions;

import org.jetbrains.annotations.NotNull;

public class StaticPageAction extends FPActionSupport {

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
