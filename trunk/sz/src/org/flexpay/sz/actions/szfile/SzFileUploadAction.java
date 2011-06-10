package org.flexpay.sz.actions.szfile;

import org.flexpay.common.actions.FPFileActionSupport;
import org.flexpay.common.persistence.file.FPFileType;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.sz.persistence.Oszn;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.OsznService;
import org.flexpay.sz.service.SzFileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Calendar;

public class SzFileUploadAction extends FPFileActionSupport {

	private String message = SUCCESS;

	private Integer year;
	private Integer month;
	private Long osznId;

	private OsznService osznService;
	private SzFileService szFileService;

	@NotNull
	@Override
	public String doExecute() {

		FPFileType fileType = fpFileService.getTypeByFileName(fpFile.getOriginalName(), moduleName);
		if (fileType == null) {
			log.warn("Unknown file type");
			fpFileService.delete(fpFile);
			return getErrorResult();
		}

		String userName = SecurityUtil.getUserName();
		try {
			SzFile szFile = new SzFile();
			szFile.setType(fileType);
			szFile.setStatus(fpFileService.getStatusByCodeAndModule(SzFile.IMPORTED_FILE_STATUS, moduleName));
			szFile.setUploadedFile(fpFile);
			szFile.setUserName(userName);

			Oszn oszn = osznService.read(osznId);
			szFile.setOszn(oszn);

			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, month);
			szFile.setDate(c.getTime());

			szFileService.create(szFile);
			log.info("File uploaded and created in DB {}", szFile);

		} catch (Exception e) {
			log.error("Unknown file type", e);
			return getErrorResult();
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		message = ERROR;
		return SUCCESS;
	}

	public String getMessage() {
		return message;
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

}
