package org.flexpay.common.actions;

import org.jetbrains.annotations.NotNull;

public class StaticPageAction extends FPActionSupport {

	@Override
	@NotNull
	protected String doExecute() throws Exception {
		return SUCCESS;
	}

	@Override
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

}
