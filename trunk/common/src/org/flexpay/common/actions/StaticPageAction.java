package org.flexpay.common.actions;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.actions.interceptor.UserPreferencesAware;
import org.apache.struts2.interceptor.SessionAware;

public class StaticPageAction extends FPActionSupport implements UserPreferencesAware, SessionAware {

	@NotNull
	protected String doExecute() throws Exception {
		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

}
