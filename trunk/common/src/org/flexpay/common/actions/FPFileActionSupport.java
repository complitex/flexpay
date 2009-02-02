package org.flexpay.common.actions;

import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import java.io.File;

import com.opensymphony.xwork2.Preparable;

/**
 * FPAction that have file uploaded
 */
public abstract class FPFileActionSupport extends FPActionSupport implements Preparable {

	private FPFileService fpFileService;

	protected FPFile fpFile = new FPFile();
	private File upload;
	private String uploadFileName;
	private String uploadContentType;

	private String moduleName;

	public void prepare() throws Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth != null ? auth.getName() : null;
		try {
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
		}
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

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public FPFile getFpFile() {
		return fpFile;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}
}
