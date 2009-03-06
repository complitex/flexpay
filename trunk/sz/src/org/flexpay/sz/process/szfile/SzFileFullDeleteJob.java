package org.flexpay.sz.process.szfile;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.sz.convert.NotSupportedOperationException;
import org.flexpay.sz.convert.SzFileUtil;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class SzFileFullDeleteJob extends Job {

	private SzFileService szFileService;

	@SuppressWarnings({"unchecked"})
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		Logger pLogger = ProcessLogger.getLogger(getClass());

		Set<Long> fileIds = (Set<Long>) parameters.get("fileIds");

		log.debug("Process szFile full delete for fileIds = {} started", fileIds);

		Collection<SzFile> szFiles = szFileService.listSzFilesByIds(fileIds);

		if (szFiles == null || szFiles.isEmpty()) {
			log.warn("Invalid File Ids");
			return RESULT_ERROR;
		}

		for (SzFile szFile : szFiles) {

			pLogger.info("Full deleting szFile with id = {} started", szFile.getId());

			try {
				SzFileUtil.deleteRecords(szFile);
			} catch (NotSupportedOperationException e) {
				log.warn("Can't delete records for file with id = {}", szFile.getId());
			}

			if (szFile.getFileToDownload() != null) {
				File responseFile = FPFileUtil.getFileOnServer(szFile.getFileToDownload());
				responseFile.delete();
			}

			File requestFile = FPFileUtil.getFileOnServer(szFile.getUploadedFile());
			requestFile.delete();

			szFileService.delete(szFile);

			pLogger.info("Full deleting szFile with id = {} finished", szFile.getId());
		}

		return RESULT_NEXT;
	}

	@Required
	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

}
