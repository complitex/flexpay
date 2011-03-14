package org.flexpay.common.action;

import org.flexpay.common.service.Roles;
import org.flexpay.common.util.SecurityUtil;
import org.jetbrains.annotations.NotNull;

public class StaticPageAdminAction extends FPActionSupport {

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (!SecurityUtil.isAuthenticationGranted(Roles.DEVELOPER)) {
			throw new IllegalStateException("Authentication required");
		}
		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

}
