package org.flexpay.common.actions;

import com.davidjc.ajaxfileupload.multipart.ProgressMonitor;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class FileUploadProgressAction implements SessionAware {

	private Map sessionMap;
	private String rnd;
	private String stringResult;

	public String execute() {
		Object mon_obj = sessionMap.get("com.davidjc.ajaxfileupload.multipart.ProgressMonitor");
		ProgressMonitor monitor = (ProgressMonitor) mon_obj;
		String progressInfo = monitor == null ? "" : monitor.percentComplete();
		setStringResult(progressInfo);

		return ActionSupport.SUCCESS;
	}

	public void setSession(Map sessionMap) {
		this.sessionMap = sessionMap;
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
