package org.flexpay.eirc.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;

import java.io.File;

public class UploadFileAction extends ActionSupport {

	@NonNls
	protected Logger log = LoggerFactory.getLogger(getClass());

	String message = "success";

	private File upload;
	private String uploadFileName;
	private String uploadContentType;

    private String moduleName;
    private FPFileService fpFileService;
	protected UserPreferences userPreferences;

	@NotNull
	public String execute() {

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

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			FPFile spFile = new FPFile();
			spFile.setModule(fpFileService.getModuleByName(moduleName));
			spFile.setOriginalName(uploadFileName);
			spFile.setUserName(userName);
			File fileOnSystem = FPFileUtil.saveToFileSystem(spFile, upload);
			spFile.setNameOnServer(fileOnSystem.getName());
			spFile.setSize(fileOnSystem.length());

			fpFileService.create(spFile);
			log.info("File uploaded {}", spFile);
		} catch (Exception e) {
			log.error("Unknown file type", e);
			setMessage(ERROR);
			return ERROR;
		}

		return SUCCESS;
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

	@Required
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

}
