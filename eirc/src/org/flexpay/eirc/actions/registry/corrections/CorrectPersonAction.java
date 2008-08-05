package org.flexpay.eirc.actions.registry.corrections;

import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class CorrectPersonAction extends FPActionSupport {
	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected String doExecute() throws Exception {
		throw new RuntimeException("Not implemented");
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
		throw new RuntimeException("Not implemented");
	}
}
