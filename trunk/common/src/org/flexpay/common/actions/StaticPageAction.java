package org.flexpay.common.actions;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.common.actions.interceptor.UserPreferencesAware;
import org.jetbrains.annotations.NotNull;

public class StaticPageAction extends FPActionSupport implements UserPreferencesAware, SessionAware {

	@NotNull
	protected String doExecute() throws Exception {
		ServletActionContext.getRequest().getSession().removeAttribute(BREADCRUMBS);
		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

}
