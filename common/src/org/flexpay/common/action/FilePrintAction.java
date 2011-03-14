package org.flexpay.common.action;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.FPFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class FilePrintAction extends FPActionSupport {

	private FPFile file = new FPFile();

	private FPFileService fpFileService;

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

		log.debug("Printing file: {}", file);

		if (file == null || file.isNew()) {
			log.warn("Incorrect file parameter");
			return SUCCESS;
		}

		Stub<FPFile> stub = stub(file);
		file = fpFileService.read(stub);
		if (file == null) {
			log.warn("Can't get fpFile with id {} from DB", stub.getId());
			return SUCCESS;
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

	public FPFile getFile() {
		return file;
	}

	public void setFile(FPFile file) {
		this.file = file;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}
}
