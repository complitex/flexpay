package org.flexpay.sz.process.szfile;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.file.FPFileStatus;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.sz.convert.NotSupportedOperationException;
import org.flexpay.sz.convert.SzFileUtil;
import org.flexpay.sz.dbf.CharacteristicDBFInfo;
import org.flexpay.sz.dbf.DBFInfo;
import org.flexpay.sz.dbf.ServiceTypeRecordDBFInfo;
import org.flexpay.sz.dbf.SubsidyRecordDBFInfo;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.persistence.ServiceTypeRecord;
import org.flexpay.sz.persistence.SubsidyRecord;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.RecordService;
import org.flexpay.sz.service.SzFileService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.flexpay.sz.process.szfile.SzFileOperationJobParameterNames.FILE_IDS;

public class SzFileLoadFromDbJob extends Job {

	private RecordService<CharacteristicRecord> characteristicRecordService;
	private RecordService<SubsidyRecord> subsidyRecordService;
	private RecordService<ServiceTypeRecord> serviceTypeRecordService;
	private SzFileService szFileService;
	private FPFileService fpFileService;

	@SuppressWarnings({"unchecked"})
	@Override
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		Logger pLogger = ProcessLogger.getLogger(getClass());

		Set<Long> fileIds = (Set<Long>) parameters.get(FILE_IDS);

		log.debug("ProcessInstance szFile load from DB for fileIds = {} started", fileIds);

		List<SzFile> szFiles = szFileService.listSzFilesByIds(fileIds);

		if (szFiles == null || szFiles.isEmpty()) {
			log.warn("Invalid File Ids");
			return RESULT_ERROR;
		}

		FPModule module = szFiles.iterator().next().getUploadedFile().getModule();
		String moduleName = module.getName();
		FPFileStatus errorStatus = fpFileService.getStatusByCodeAndModule(SzFile.PROCESSED_WITH_ERRORS_FILE_STATUS, moduleName);

		for (SzFile szFile : szFiles) {

			pLogger.info("Loading from DB szFile with id = {} started", szFile.getId());

			try {
				if (!SzFileUtil.isLoadedToDb(szFile)) {
					pLogger.error("SzFile with id {} not loaded to DB", szFile.getId());
					FPFileStatus status = fpFileService.getStatusByCodeAndModule(SzFile.PROCESSED_WITH_ERRORS_FILE_STATUS, szFile.getUploadedFile().getModule().getName());
					szFile.setStatus(status);
					szFileService.update(szFile);
					continue;
				}
			} catch (NotSupportedOperationException e) {
				// do nothing
			}

			FPFile fileToDownload = szFile.getFileToDownload();
			FPFile uploadedFile = szFile.getUploadedFile();

			if (fileToDownload != null) {
				szFile.setFileToDownload(null);
				szFile.setStatus(fpFileService.getStatusByCodeAndModule(SzFile.PROCESSING_FILE_STATUS, moduleName));
				szFileService.update(szFile);
				fpFileService.delete(fileToDownload);
			}

			fileToDownload = new FPFile();
			fileToDownload.setUserName(szFile.getUserName());
			fileToDownload.setOriginalName(uploadedFile.getOriginalName());
			fileToDownload.setModule(module);

			try {
				FPFileUtil.copy(new File(uploadedFile.getOriginalName()), fileToDownload);

				Long szFileTypeCode = szFile.getType().getCode();

				if (SzFile.CHARACTERISTIC_FILE_TYPE.equals(szFileTypeCode)) {
					DBFInfo<?> dbfInfo = new CharacteristicDBFInfo(uploadedFile);
					SzFileUtil.loadFromDbGeneric(szFile, fileToDownload, dbfInfo, characteristicRecordService);
				} else if (SzFile.SUBSIDY_FILE_TYPE.equals(szFileTypeCode)) {
					DBFInfo<?> dbfInfo = new SubsidyRecordDBFInfo(uploadedFile);
					SzFileUtil.loadFromDbGeneric(szFile, fileToDownload, dbfInfo, subsidyRecordService);
				} else if (SzFile.SRV_TYPES_FILE_TYPE.equals(szFileTypeCode)) {
					DBFInfo<?> dbfInfo = new ServiceTypeRecordDBFInfo(uploadedFile);
					SzFileUtil.loadFromDbGeneric(szFile, fileToDownload, dbfInfo, serviceTypeRecordService);
				} else {
					throw new NotSupportedOperationException();
				}

				szFile.setStatus(fpFileService.getStatusByCodeAndModule(SzFile.PROCESSED_FILE_STATUS, moduleName));
				szFile.setFileToDownload(fileToDownload);
				szFileService.update(szFile);
			} catch (IOException e) {
				log.error("Can't create file on system", e);
				szFile.setStatus(errorStatus);
				szFile.setFileToDownload(null);
				szFileService.update(szFile);
			} catch (NotSupportedOperationException e) {
				log.error("Error loading data from DB and creating file to download", e);
				szFile.setStatus(errorStatus);
				szFile.setFileToDownload(null);
				szFileService.update(szFile);
				fpFileService.delete(fileToDownload);
			}

			pLogger.info("Loading from DB szFile with id = {} finished", szFile.getId());
		}

		log.debug("ProcessInstance szFile load from DB for fileIds = {} finihed", fileIds);

		return RESULT_NEXT;
	}

	@Required
	public void setCharacteristicRecordService(RecordService<CharacteristicRecord> characteristicRecordService) {
		this.characteristicRecordService = characteristicRecordService;
	}

	@Required
	public void setSubsidyRecordService(RecordService<SubsidyRecord> subsidyRecordService) {
		this.subsidyRecordService = subsidyRecordService;
	}

	@Required
	public void setServiceTypeRecordService(RecordService<ServiceTypeRecord> serviceTypeRecordService) {
		this.serviceTypeRecordService = serviceTypeRecordService;
	}

	@Required
	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

}
