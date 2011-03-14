package org.flexpay.eirc.action.spfile;

import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryFileService;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class SpFilesListAction extends FPActionWithPagerSupport<FPFile> {

	private List<FPFile> spFilesList = list();

    private String moduleName;
    private RegistryFileService registryFileService;
	private FPFileService fpFileService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		spFilesList = fpFileService.getFilesByModuleName(moduleName, getPager());

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

	public List<FPFile> getSpFilesList() {
		return spFilesList;
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
