package org.flexpay.sz.actions;

import java.util.List;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.sz.persistence.ImportFile;
import org.flexpay.sz.service.ImportFileService;

public class ImportFileListAction extends CommonAction {
	
	private ImportFileService importFileService;
	
	private List<ImportFile> importFileList;
	
	public String execute() throws FlexPayException {
		
		importFileList = importFileService.getEntities();
		
		
		
		return "success";
	}

	public void setImportFileService(ImportFileService importFileService) {
		this.importFileService = importFileService;
	}

	public List<ImportFile> getImportFileList() {
		return importFileList;
	}

	public void setImportFileList(List<ImportFile> importFileList) {
		this.importFileList = importFileList;
	}

}
