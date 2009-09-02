package org.flexpay.common.actions.processing;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ProcessDefinitionDeployAction extends FPActionSupport {

	private File upload;

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
			is = new FileInputStream(upload);
			processManager.deployProcessDefinition(is, true);
			addActionError(getText("common.processing.deployment_success"));
		} finally {
			IOUtils.closeQuietly(is);
			upload.delete();
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

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
