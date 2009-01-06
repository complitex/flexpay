package org.flexpay.sz.process.szfile;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.sz.convert.NotSupportedOperationException;
import org.flexpay.sz.convert.SzFileUtil;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

public class FileDeleteJob extends Job {

	private SzFileService szFileService;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		Long fileId = (Long) parameters.get("fileId");

		SzFile szFile = szFileService.readFull(fileId);

		if (szFile == null) {
			log.warn("Invalid File Id");
			return RESULT_ERROR;
		}

		// delete records
		try {
			SzFileUtil.deleteRecords(szFile);
		} catch (NotSupportedOperationException e) {
			// ignore
		}

		// delete response file
		if (szFile.getFileToDownload() != null) {
			File responseFile = FPFileUtil.getFileOnServer(szFile.getFileToDownload());
			responseFile.delete();
		}

		// delete request file and parent dir if no more files in this dir
		File requestFile = FPFileUtil.getFileOnServer(szFile.getUploadedFile());
		requestFile.delete();

		// delete SzFile
		szFileService.delete(szFile);

		return RESULT_NEXT;
	}

	@Required
	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

}
