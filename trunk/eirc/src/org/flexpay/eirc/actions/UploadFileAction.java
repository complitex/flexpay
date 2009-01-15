package org.flexpay.eirc.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.common.actions.interceptor.UserPreferencesAware;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.util.Map;

public class UploadFileAction implements UserPreferencesAware, SessionAware {

	@NonNls
	protected Logger log = LoggerFactory.getLogger(getClass());

	private Map sessionMap;
	private String rnd;
	private String stringResult;
	private File upload;
	private String uploadFileName;
	private String uploadContentType;
	private FPFile spFile;

    private String moduleName;
    private FPFileService fpFileService;
	protected UserPreferences userPreferences;

	@NotNull
	public String execute() {
		if (uploadFileName == null) {
			return ActionSupport.SUCCESS;
		}

		try {
			spFile = new FPFile();
			spFile.setModule(fpFileService.getModuleByName(moduleName));
			spFile.setOriginalName(uploadFileName);
			spFile.setUserName(getUserPreferences().getUserName());
			File fileOnSystem = FPFileUtil.saveToFileSystem(spFile, upload);
			spFile.setNameOnServer(fileOnSystem.getName());
			spFile.setSize(fileOnSystem.length());

			log.debug("Creating FlexPayFile: {}", spFile);
			fpFileService.create(spFile);
			log.info("File uploaded {}", spFile);
		} catch (Exception e) {
			log.error("Unknown file type", e);
		}

		return ActionSupport.SUCCESS;
	}

	public FPFile getSpFile() {
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

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setSession(Map sessionMap) {
		this.sessionMap = sessionMap;
	}

	public String getRnd() {
		return rnd;
	}

	public void setRnd(String rnd) {
		this.rnd = rnd;
	}

	public String getStringResult() {
		return stringResult;
	}

	public void setStringResult(String stringResult) {
		this.stringResult = stringResult;
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
