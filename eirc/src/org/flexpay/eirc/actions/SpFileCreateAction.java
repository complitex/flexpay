package org.flexpay.eirc.actions;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;

import java.io.File;
import java.util.Date;

public class SpFileCreateAction extends FPActionSupport {
	private File upload;
	private String uploadFileName;

	private SpFileService spFileService;
	private boolean isUploaded = false;

	public String doExecute() throws Exception {
		if (isSubmit()) {
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

		return INPUT;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@Override
	protected String getErrorResult() {
		return INPUT;
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

	public void setSpFileService(SpFileService spFileService) {
		this.spFileService = spFileService;
	}

	public boolean isUploaded() {
		return isUploaded;
	}
}
