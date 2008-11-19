package org.flexpay.sz.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.sz.convert.SzFileUtil;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.jetbrains.annotations.NotNull;

public class LoadToDBAction extends FPActionSupport {

	private Long szFileId;
	private String action;

	private SzFileService szFileService;

	@NotNull
	public String doExecute() throws Exception {
		SzFile szFile = szFileService.readFull(szFileId);
		if ("loadToDb".equals(action)) {
			SzFileUtil.loadToDb(szFile);
		} else if ("loadFromDb".equals(action)) {
			SzFileUtil.loadFromDb(szFile);
		} else if ("deleteFromDb".equals(action)) {
			SzFileUtil.deleteRecords(szFile);
		} else if ("fullDelete".equals(action)) {
			SzFileUtil.delete(szFile);
		}

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setSzFileId(Long szFileId) {
		this.szFileId = szFileId;
	}

	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
