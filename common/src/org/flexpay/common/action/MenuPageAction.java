package org.flexpay.common.action;

import org.jetbrains.annotations.NotNull;

public class MenuPageAction extends FPActionSupport {

	@Override
	@NotNull
	protected String doExecute() throws Exception {
		getUserPreferences().getCrumbs().removeAllElements();
		return SUCCESS;
	}

	@Override
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

}
