package org.flexpay.common.actions;

import org.jetbrains.annotations.NotNull;

public class StaticPageAction extends FPActionSupport {

	@NotNull
	protected String doExecute() throws Exception {
		getUserPreferences().getCrumbs().removeAllElements();
		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

}
