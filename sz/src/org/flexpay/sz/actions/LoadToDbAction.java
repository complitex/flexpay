package org.flexpay.sz.actions;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.sz.convert.SzFileUtil;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;

public class LoadToDbAction extends CommonAction {

	private Long szFileId;
	private String action;

	private SzFileService szFileService;

	public String execute() throws Throwable {
		SzFile szFile = szFileService.readFull(szFileId);
		if ("loadToDb".equals(action)) {
			SzFileUtil.loadToDb(szFile);
		} else if ("loadFromDb".equals(action)) {
			SzFileUtil.loadFromDb(szFile);
		} else if ("deleteFromDb".equals(action)) {
			SzFileUtil.deleteRecords(szFile);
		}

		return "success";
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
