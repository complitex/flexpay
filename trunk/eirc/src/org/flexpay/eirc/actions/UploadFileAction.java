package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.SecurityUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;

public class UploadFileAction extends FPActionSupport {

	String message = "success";

	private FPFile fpFile;
	private File upload;
	private String uploadFileName;
	private String uploadContentType;

	private String moduleName;
	private FPFileService fpFileService;

	@NotNull
	public String doExecute() {

		if (uploadFileName == null) {
			log.warn("Error: uploadFileName is null");
			setMessage(ERROR);
			return ERROR;
		}
/*
		FPFileType fileType = fpFileService.getTypeByFileName(uploadFileName, moduleName);
		if (fileType == null) {
			log.warn("Unknown file type");
			setMessage(ERROR);
			return ERROR;
		}
*/

		String userName = SecurityUtil.getUserName();
		try {
			fpFile = new FPFile();
			fpFile.setModule(fpFileService.getModuleByName(moduleName));
			fpFile.setOriginalName(uploadFileName);
			fpFile.setUserName(userName);
			File fileOnSystem = FPFileUtil.saveToFileSystem(fpFile, upload);
			fpFile.setNameOnServer(fileOnSystem.getName());
			fpFile.setSize(fileOnSystem.length());

			fpFileService.create(fpFile);
			log.info("File uploaded {}", fpFile);
		} catch (Exception e) {
			log.error("Unknown file type", e);
			setMessage(ERROR);
			return ERROR;
		}

		return SUCCESS;
	}

	protected void setBreadCrumbs() {
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
		return ERROR;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public FPFile getFpFile() {
		return fpFile;
	}

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

}
