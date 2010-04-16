package org.flexpay.common.actions;

import org.jetbrains.annotations.NotNull;

public class UserPreferencesUpdateAction extends FPActionSupport {

	private String prop;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
    @Override
	protected String doExecute() throws Exception {
		if (prop != null) {
			getUserPreferences().setTestProp(prop);
		}

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

	public String getProp() {
		return prop;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}
}
