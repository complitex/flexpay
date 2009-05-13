package org.flexpay.sz.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.file.FPFileType;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.SecurityUtil;
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
import java.util.Calendar;

public class UploadFileAction extends ActionSupport {

	@NonNls
	protected Logger log = LoggerFactory.getLogger(getClass());

	String message = "success";

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

	@NotNull
	public String execute() {

		if (uploadFileName == null) {
			log.warn("Error: uploadFileName is null");
			setMessage(ERROR);
			return ERROR;
		}
		FPFileType fileType = fpFileService.getTypeByFileName(uploadFileName, moduleName);
		if (fileType == null) {
			log.warn("Unknown file type");
			setMessage(ERROR);
			return ERROR;
		}

		String userName = SecurityUtil.getUserName();
		try {
			SzFile szFile = new SzFile();
			szFile.setType(fileType);
			szFile.setStatus(fpFileService.getStatusByCodeAndModule(SzFile.IMPORTED_FILE_STATUS, moduleName));
			FPFile fileOnServer = new FPFile();
			fileOnServer.setModule(fpFileService.getModuleByName(moduleName));
			fileOnServer.setOriginalName(uploadFileName);
			fileOnServer.setUserName(userName);
			File fileOnSystem = FPFileUtil.saveToFileSystem(fileOnServer, upload);
			fileOnServer.setNameOnServer(fileOnSystem.getName());
			fileOnServer.setSize(fileOnSystem.length());
			szFile.setUploadedFile(fileOnServer);
			Oszn oszn = osznService.read(osznId);
			szFile.setOszn(oszn);
			szFile.setUserName(userName);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, month);
			szFile.setDate(c.getTime());

			szFileService.create(szFile);
			log.info("File uploaded {}", szFile);
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
