package org.flexpay.eirc.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class QuittancePayAction extends FPActionSupport {

	@NotNull
	public String doExecute() {

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

}
