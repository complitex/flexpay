package org.flexpay.sz.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.opensymphony.xwork2.ActionSupport;

public class UploadFilesAction extends ActionSupport {
	private File upload;
	private String uploadFileName;

	public String execute() throws Exception {
		try {
			String fullFileName = "c:/myfile.txt";
			File theFile = new File(fullFileName);
			FileUtils.copyFile(upload, theFile);
		} catch (Exception e) {
			addActionError(e.getMessage());
		}

		return "form";

	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

}
