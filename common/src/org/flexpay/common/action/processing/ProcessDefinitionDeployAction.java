package org.flexpay.common.action.processing;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.action.FPFileActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.FileInputStream;
import java.io.InputStream;

public class ProcessDefinitionDeployAction extends FPFileActionSupport {

	private ProcessManager processManager;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in
	 * a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (isNotSubmit()) {
			return INPUT;
		}

		InputStream is = null;
		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			is = new FileInputStream(getUpload());
			processManager.deployProcessDefinition(is, true);
			addActionMessage(getText("common.processing.deployment_success"));
		} finally {
			IOUtils.closeQuietly(is);
		}

		return REDIRECT_SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in
	 * a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
