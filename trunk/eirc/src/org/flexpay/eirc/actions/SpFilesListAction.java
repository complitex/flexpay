package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.FPFileService;
import org.flexpay.eirc.service.RegistryFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class SpFilesListAction extends FPActionSupport {

    private String moduleName;
    private RegistryFileService registryFileService;
	private FPFileService fpFileService;
	private List<FPFile> spFileList;

	@NotNull
	public String doExecute() throws FlexPayException {

		spFileList = fpFileService.getFilesByModuleName(moduleName);

		return SUCCESS;
	}

	/**
	 * Check if registry file is loaded to database
	 *
	 * @param fileId Registry file id
	 * @return <code>true</code> if file already loaded, or <code>false</code> otherwise
	 */
	public boolean isLoaded(@NotNull Long fileId) {
		boolean loaded = registryFileService.isLoaded(new Stub<FPFile>(fileId));

		log.debug("File was {} loaded: {}", (loaded ? "" : "not"), fileId);

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

	public List<FPFile> getSpFileList() {
		return spFileList;
	}

    @Required
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Required
    public void setRegistryFileService(RegistryFileService registryFileService) {
        this.registryFileService = registryFileService;
    }

    @Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

}
