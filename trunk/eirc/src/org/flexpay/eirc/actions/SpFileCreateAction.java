package org.flexpay.eirc.actions;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FlexPayFile;
import org.flexpay.common.service.FlexPayFileService;
import org.flexpay.common.util.FlexPayFileUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.util.Date;

public class SpFileCreateAction extends FPActionSupport {

	private File upload;
	private String uploadFileName;

    private String moduleName;
    private FlexPayFileService flexPayFileService;

	private boolean isUploaded = false;

	private FlexPayFile spFile;

	@NotNull
	public String doExecute() throws Exception {
		if (isSubmit()) {
			if (StringUtils.isNotEmpty(uploadFileName)) {
				spFile = new FlexPayFile();
                spFile.setModule(flexPayFileService.getModuleByName(moduleName));
                spFile.setCreationDate(new Date());
                spFile.setOriginalName(uploadFileName);
                spFile.setUserName(getUserPreferences().getUserName());
                File fileOnSystem = FlexPayFileUtil.saveToFileSystem(spFile, upload);
                spFile.setNameOnServer(fileOnSystem.getName());
                spFile.setSize(fileOnSystem.length());

				try {
					if (log.isDebugEnabled()) {
						log.debug("Creating RegistryFile: " + spFile);
					}

					flexPayFileService.create(spFile);
				} catch (FlexPayException e) {
                    flexPayFileService.deleteFromFileSystem(spFile);
					throw e;
				}

				isUploaded = true;
			}
		}

		return INPUT;
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
		return INPUT;
	}

	public FlexPayFile getSpFile() {
		return spFile;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public File getUpload() {
		return upload;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

    public boolean isUploaded() {
		return isUploaded;
	}

    @Required
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Required
    public void setFlexPayFileService(FlexPayFileService flexPayFileService) {
        this.flexPayFileService = flexPayFileService;
    }

}
