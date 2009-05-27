package org.flexpay.common.actions;

import org.apache.struts2.ServletActionContext;
import org.jetbrains.annotations.NotNull;

public class StaticPageAction extends FPActionSupport {

	@NotNull
	protected String doExecute() throws Exception {
		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

}
