package org.flexpay.sz.actions;

import java.io.File;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.davidjc.ajaxfileupload.action.FileUpload;
import com.opensymphony.xwork2.Action;

public class AjaxFileUploadAction extends FileUpload {

	private final Logger logger = Logger.getLogger(AjaxFileUploadAction.class);

	public String execute() {
		File uploadedFile = this.getUpload();
		String contentType = this.getUploadContentType();
		String fileName = this.getUploadFileName();

		return Action.SUCCESS;
	}

}
