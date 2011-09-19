package org.flexpay.common.action;

import org.flexpay.common.progressbar.ProgressMonitor;
import org.jetbrains.annotations.NotNull;

public class FileUploadProgressAction extends FPActionSupport {

	private String rnd;
	private String stringResult;

	@NotNull
	public String doExecute() {
		Object mon_obj = session.get(ProgressMonitor.SESSION_PROGRESS_MONITOR);
        log.debug("mon_obj = {}", mon_obj);
		ProgressMonitor monitor = (ProgressMonitor) mon_obj;
        log.debug("moninor = {}", monitor);
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
