package org.flexpay.eirc.action.spfile;

import org.flexpay.common.action.FPFileActionSupport;
import org.jetbrains.annotations.NotNull;

public class SpFileUploadAction extends FPFileActionSupport {

	private String message = SUCCESS;

	@NotNull
	@Override
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
		message = ERROR;
		return SUCCESS;
	}

	public String getMessage() {
		return message;
	}

}
