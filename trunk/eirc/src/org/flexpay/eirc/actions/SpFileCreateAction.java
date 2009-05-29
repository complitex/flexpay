package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class SpFileCreateAction extends FPActionSupport {

	@NotNull
	public String doExecute() throws Exception {
		return INPUT;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return INPUT;
	}

}
