package org.flexpay.eirc.actions.processing;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

public class DeployProcessDefinitionAction extends FPActionSupport {

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
	protected String doExecute() throws Exception {

		if (isSubmit()) {
			InputStream is = null;
			try {
				//noinspection IOResourceOpenedButNotSafelyClosed
				is = new FileInputStream(upload);
				processManager.deployProcessDefinition(is, true);
				addActionError(getText("eirc.processing.deployment_success"));
				return REDIRECT_SUCCESS;
			} finally {
				IOUtils.closeQuietly(is);
				upload.delete();
			}
		}

		return INPUT;
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
	protected String getErrorResult() {
		return INPUT;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
