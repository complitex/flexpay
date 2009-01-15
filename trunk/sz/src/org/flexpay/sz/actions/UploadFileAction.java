package org.flexpay.sz.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.common.actions.interceptor.UserPreferencesAware;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.FPFileType;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.sz.persistence.Oszn;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.OsznService;
import org.flexpay.sz.service.SzFileService;
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
	private Integer year;
	private Integer month;
	private Long osznId;

    private String moduleName;
	private OsznService osznService;
	private SzFileService szFileService;
    private FPFileService fpFileService;
	protected UserPreferences userPreferences;

	@NotNull
	public String execute() {
		if (uploadFileName == null) {
			setStringResult(ActionSupport.ERROR);
			return ActionSupport.SUCCESS;
		}
		FPFileType fileType = fpFileService.getTypeByFileName(uploadFileName, moduleName);
		if (fileType == null) {
			log.warn("Unknown file type");
			setStringResult(ActionSupport.ERROR);
			return ActionSupport.SUCCESS;
		}

		try {
			SzFile szFile = new SzFile();
			szFile.setType(fileType);
			szFile.setStatus(fpFileService.getStatusByCodeAndModule(SzFile.IMPORTED_FILE_STATUS, moduleName));
			FPFile fileOnServer = new FPFile();
			fileOnServer.setModule(fpFileService.getModuleByName(moduleName));
			fileOnServer.setOriginalName(uploadFileName);
			fileOnServer.setUserName(getUserPreferences().getUserName());
			File fileOnSystem = FPFileUtil.saveToFileSystem(fileOnServer, upload);
			fileOnServer.setNameOnServer(fileOnSystem.getName());
			fileOnServer.setSize(fileOnSystem.length());
			szFile.setUploadedFile(fileOnServer);
			Oszn oszn = osznService.read(osznId);
			szFile.setOszn(oszn);
			szFile.setUserName(getUserPreferences().getUserName());
			szFile.setFileYear(year);
			szFile.setFileMonth(month);

			szFileService.create(szFile);
			log.info("File uploaded {}", szFile);
			setStringResult(ActionSupport.SUCCESS);
		} catch (Exception e) {
			log.error("Unknown file type", e);
			setStringResult(ActionSupport.ERROR);
		}

		return ActionSupport.SUCCESS;
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

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setOsznId(Long osznId) {
		this.osznId = osznId;
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
    public void setOsznService(OsznService osznService) {
        this.osznService = osznService;
    }

	@Required
    public void setSzFileService(SzFileService szFileService) {
        this.szFileService = szFileService;
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
