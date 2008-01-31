package org.flexpay.sz.actions;

import org.flexpay.sz.convert.SzFileLoader;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;

public class LoadFromDbAction {

	private Long szFileId;

	private SzFileService szFileService;
	private SzFileLoader szFileLoader;

	public String execute() throws Throwable {
		SzFile importFile = szFileService.readFull(szFileId);
		szFileLoader.loadFromDb(importFile);

		return "success";
	}

	public void setSzFileLoader(SzFileLoader szFileLoader) {
		this.szFileLoader = szFileLoader;
	}

	public void setSzFileId(Long szFileId) {
		this.szFileId = szFileId;
	}

	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

}
