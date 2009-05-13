package org.flexpay.common.actions;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;

/**
 * FPAction that have file uploaded
 */
public abstract class FPFileActionSupport extends FPActionSupport {

	private FPFileService fpFileService;

	protected FPFile fpFile = new FPFile();
	private File upload;
	private String uploadFileName;
	private String uploadContentType;

	private String moduleName;

	public String execute() throws Exception {

		String userName = SecurityUtil.getUserName();
		try {

			log.debug("Preparing file action, params: FileName - {}, ContentType - {}, ModuleName - {}",
					new String[]{uploadFileName, uploadContentType, moduleName});

			FPModule module = fpFileService.getModuleByName(moduleName);
			if (module == null) {
				throw new Exception("Unknown module " + moduleName);
			}
			fpFile.setModule(module);
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

		return super.execute();
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

	public String getUploadFileName() {
		return uploadFileName;
	}

	public String getModuleName() {
		return moduleName;
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
