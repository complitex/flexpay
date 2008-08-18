package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpFileListAction extends FPActionSupport {

	private SpFileService spFileService;
	private List<SpFile> spFileList;

	@NotNull
	public String doExecute() throws FlexPayException {

		spFileList = spFileService.getEntities();

		return SUCCESS;
	}

	/**
	 * Check if registry file is loaded to database
	 *
	 * @param fileId Registry file id
	 * @return <code>true</code> if file already loaded, or <code>false</code> otherwise
	 */
	public boolean isLoaded(@NotNull Long fileId) {
		boolean loaded = spFileService.isLoaded(new Stub<SpFile>(fileId));

		if (log.isDebugEnabled()) {
			log.debug("File was " + (loaded ? "" : "not") + " loaded: " + fileId);
		}

		return loaded;
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
		return SUCCESS;
	}

	public void setSpFileService(SpFileService spFileService) {
		this.spFileService = spFileService;
	}

	public List<SpFile> getSpFileList() {
		return spFileList;
	}
}
