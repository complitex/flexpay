package org.flexpay.sz.actions;

import org.flexpay.sz.convert.SzFileUtil;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.springframework.beans.factory.annotation.Required;

public class LoadFromDBAction {

	private Long szFileId;

	private SzFileService szFileService;

	public String execute() throws Throwable {
		SzFile importFile = szFileService.readFull(szFileId);

		SzFileUtil.loadFromDb(importFile);

		return "success";
	}

	public void setSzFileId(Long szFileId) {
		this.szFileId = szFileId;
	}

	@Required
	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

}
