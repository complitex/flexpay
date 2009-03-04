package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.history.HistoryUnPacker;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

public class HistoryUnPackJob extends Job {

	public static final String PARAM_FILE_ID = "fileId";

	private FPFileService fileService;
	private HistoryUnPacker unPacker;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		Long fileId = (Long) parameters.get(PARAM_FILE_ID);
		if (fileId == null) {
			throw new FlexPayException("No file id");
		}
		FPFile file = fileService.read(fileId);
		try {
			unPacker.unpackHistory(file);
		} catch (Exception ex) {
			throw new FlexPayException("unpack failed", ex);
		}

		return RESULT_NEXT;
	}

	@Required
	public void setFileService(FPFileService fileService) {
		this.fileService = fileService;
	}

	@Required
	public void setUnPacker(HistoryUnPacker unPacker) {
		this.unPacker = unPacker;
	}
}
