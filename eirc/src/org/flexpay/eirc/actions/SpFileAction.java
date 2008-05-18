package org.flexpay.eirc.actions;

import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;
import org.flexpay.eirc.sp.SpFileFormatException;
import org.flexpay.eirc.sp.SpFileUtil;

public class SpFileAction {

	private Long spFileId;
	private String action;
	private SpFileFormatException spFileFormatException;

	private SpFileService spFileService;

	public String execute() throws Exception {
		SpFile spFile = spFileService.read(spFileId);
		if ("loadToDb".equals(action)) {
			try {
				SpFileUtil.loadToDb(spFile);
			} catch (SpFileFormatException e) {
				spFileFormatException = e;
			}
		} else if ("loadFromDb".equals(action)) {
			// SzFileUtil.loadFromDb(szFile);
		} else if ("deleteFromDb".equals(action)) {
			// SzFileUtil.deleteRecords(szFile);
		} else if ("fullDelete".equals(action)) {
			// SzFileUtil.delete(szFile);
		}

		return "success";
	}

	/**
	 * @param spFileId the spFileId to set
	 */
	public void setSpFileId(Long spFileId) {
		this.spFileId = spFileId;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @param spFileService the spFileService to set
	 */
	public void setSpFileService(SpFileService spFileService) {
		this.spFileService = spFileService;
	}

	/**
	 * @return the spFileFormatException
	 */
	public SpFileFormatException getSpFileFormatException() {
		return spFileFormatException;
	}
}
