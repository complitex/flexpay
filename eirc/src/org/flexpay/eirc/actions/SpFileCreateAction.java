package org.flexpay.eirc.actions;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;
import org.apache.commons.lang.StringUtils;

public class SpFileCreateAction extends CommonAction {
	private File upload;
	private String uploadFileName;
	private String contentType;

	private SpFileService spFileService;
	private boolean isUploaded = false;

	public String execute() throws IOException, FlexPayException {
		if (isSubmitted()) {
			if (StringUtils.isNotEmpty(uploadFileName)) {
				SpFile spFile = new SpFile();
				spFile.saveToFileSystem(upload);

				spFile.setUserName("vld");
				spFile.setRequestFileName(uploadFileName);
				spFile.setImportDate(new Date());
				try {
					spFileService.create(spFile);
				} catch (FlexPayException e) {
					spFile.getRequestFile().delete();
					throw e;
				}

				isUploaded = true;
			}
		}

		return "form";
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

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setSpFileService(SpFileService spFileService) {
		this.spFileService = spFileService;
	}

	public boolean isUploaded() {
		return isUploaded;
	}

}
