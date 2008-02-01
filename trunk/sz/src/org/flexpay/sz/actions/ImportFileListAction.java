package org.flexpay.sz.actions;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;

public class ImportFileListAction extends CommonAction {
	
	private SzFileService szFileService;
	
	private List<SzFile> importFileList;
	
	public String execute() throws FlexPayException {
		
		importFileList = szFileService.getEntities();
		
		
		
		return "success";
	}

	public List<SzFile> getImportFileList() {
		return importFileList;
	}

	public void setImportFileList(List<SzFile> importFileList) {
		this.importFileList = importFileList;
	}

	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}
	
	public String getSzDataRoot() throws IOException {
		return ApplicationConfig.getInstance().getSzDataRoot().getCanonicalPath();
	}
	
	public String getSeparator() {
		return File.separator;
	}
	
}
