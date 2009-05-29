package org.flexpay.common.actions;

import com.davidjc.ajaxfileupload.multipart.ProgressMonitor;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FileUploadProgressAction extends FPActionSupport {

	private String rnd;
	private String stringResult;

	public String doExecute() {
		Object mon_obj = session.get("com.davidjc.ajaxfileupload.multipart.ProgressMonitor");
		ProgressMonitor monitor = (ProgressMonitor) mon_obj;
		String progressInfo = monitor == null ? "" : monitor.percentComplete();
		log.debug("progressInfo = {}", progressInfo);
		setStringResult(progressInfo);

		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	@Override
	protected void setBreadCrumbs() {
		
	}

	public void setRnd(String rnd) {
		this.rnd = rnd;
	}

	public String getRnd() {
		return rnd;
	}

	public void setStringResult(String stringResult) {
		this.stringResult = stringResult;
	}

	public String getStringResult() {
		return stringResult;
	}

}
