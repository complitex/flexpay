package org.flexpay.eirc.actions;

import java.io.IOException;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.SpRegistryTypeService;
import org.flexpay.eirc.sp.SpFileFormatException;
import org.flexpay.eirc.sp.SpFileParser;

public class SpFileAction {

	private Long spFileId;
	private String action;
	private SpFileFormatException spFileFormatException;

	private SpFileService spFileService;
	private SpRegistryService spRegistryService;
	private SpRegistryRecordService spRegistryRecordService;
	private SpRegistryTypeService spRegistryTypeService;

	public String execute() throws IOException, SpFileFormatException,
			FlexPayException {
		SpFile spFile = spFileService.read(spFileId);
		if ("loadToDb".equals(action)) {
			SpFileParser spFileParser = new SpFileParser(spFile);
			spFileParser.setSpRegistryService(spRegistryService);
			spFileParser.setSpRegistryTypeService(spRegistryTypeService);
			spFileParser.setSpRegistryRecordService(spRegistryRecordService);
			try {
				spFileParser.parse();
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
	 * @param spFileId
	 *            the spFileId to set
	 */
	public void setSpFileId(Long spFileId) {
		this.spFileId = spFileId;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @param spRegistryService
	 *            the spRegistryService to set
	 */
	public void setSpRegistryService(SpRegistryService spRegistryService) {
		this.spRegistryService = spRegistryService;
	}

	/**
	 * @param spRegistryRecordService
	 *            the spRegistryRecordService to set
	 */
	public void setSpRegistryRecordService(
			SpRegistryRecordService spRegistryRecordService) {
		this.spRegistryRecordService = spRegistryRecordService;
	}

	/**
	 * @param spRegistryTypeService
	 *            the spRegistryTypeService to set
	 */
	public void setSpRegistryTypeService(
			SpRegistryTypeService spRegistryTypeService) {
		this.spRegistryTypeService = spRegistryTypeService;
	}

	/**
	 * @param spFileService
	 *            the spFileService to set
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
