package org.flexpay.eirc.actions;

import java.util.List;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;

public class SpFileListAction {
	private SpFileService spFileService;
	private List<SpFile> spFileList;
	
	public String execute() throws FlexPayException {
		spFileList = spFileService.getEntities();

		return "success";
	}

	public void setSpFileService(SpFileService spFileService) {
		this.spFileService = spFileService;
	}

	public List<SpFile> getSpFileList() {
		return spFileList;
	}

}
