package org.flexpay.sz.actions;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.sz.convert.SzFileLoader;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;

public class LoadToDbAction extends CommonAction {
	
	private Long szFileId;
	
	private SzFileService szFileService;
	private SzFileLoader szFileLoader;
	
	public String execute() throws Exception {
		SzFile importFile = szFileService.readFull(szFileId);
		szFileLoader.loadToDb(importFile);
		
		return "";
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
