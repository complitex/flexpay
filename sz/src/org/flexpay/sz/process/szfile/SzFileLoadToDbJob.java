package org.flexpay.sz.process.szfile;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.file.FPFileStatus;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.sz.convert.NotSupportedOperationException;
import org.flexpay.sz.convert.SzFileUtil;
import org.flexpay.sz.dbf.CharacteristicDBFInfo;
import org.flexpay.sz.dbf.ServiceTypeRecordDBFInfo;
import org.flexpay.sz.dbf.SubsidyRecordDBFInfo;
import org.flexpay.sz.dbf.SzDbfReader;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.persistence.ServiceTypeRecord;
import org.flexpay.sz.persistence.SubsidyRecord;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.RecordService;
import org.flexpay.sz.service.SzFileService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.flexpay.sz.process.szfile.SzFileOperationJobParameterNames.FILE_IDS;

public class SzFileLoadToDbJob extends Job {

	private RecordService<CharacteristicRecord> characteristicRecordService;
	private RecordService<ServiceTypeRecord> serviceTypeRecordService;
	private RecordService<SubsidyRecord> subsidyRecordService;
	private SzFileService szFileService;
	private FPFileService fpFileService;

	@SuppressWarnings ({"unchecked"})
	@Override
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		Logger pLogger = ProcessLogger.getLogger(getClass());

		Set<Long> fileIds = (Set<Long>) parameters.get(FILE_IDS);

		log.debug("Process szFile load to DB for fileIds = {} started", fileIds);

		List<SzFile> szFiles = szFileService.listSzFilesByIds(fileIds);

		if (szFiles == null || szFiles.isEmpty()) {
			log.warn("Invalid File Ids");
			return RESULT_ERROR;
		}

		for (SzFile szFile : szFiles) {

			pLogger.info("Loading to DB szFile with id = {} started", szFile.getId());

			try {
				SzFileUtil.deleteRecords(szFile);
			} catch (NotSupportedOperationException e) {
				log.warn("Can't delete records for file with id = {}", szFile.getId());
			}

			FPFile file = szFile.getUploadedFile();
			Long szFileTypeCode = szFile.getType().getCode();

			SzDbfReader<?, ?> reader = null;

			String moduleName = szFile.getUploadedFile().getModule().getName();

			try {
				if (SzFile.CHARACTERISTIC_FILE_TYPE.equals(szFileTypeCode)) {
					CharacteristicDBFInfo dbfInfo = new CharacteristicDBFInfo(file);
					reader = new SzDbfReader<CharacteristicRecord, CharacteristicDBFInfo>(dbfInfo, file);
					CharacteristicRecord record;
					while ((record = (CharacteristicRecord) reader.read()) != null) {
						record.setSzFile(szFile);
						characteristicRecordService.create(record);
					}
				} else if (SzFile.SUBSIDY_FILE_TYPE.equals(szFileTypeCode)) {
					SubsidyRecordDBFInfo dbfInfo = new SubsidyRecordDBFInfo(file);
					reader = new SzDbfReader<SubsidyRecord, SubsidyRecordDBFInfo>(dbfInfo, file);
					SubsidyRecord record;
					while ((record = (SubsidyRecord) reader.read()) != null) {
						record.setSzFile(szFile);
						subsidyRecordService.create(record);
					}
				} else if (SzFile.SRV_TYPES_FILE_TYPE.equals(szFileTypeCode)) {
					ServiceTypeRecordDBFInfo dbfInfo = new ServiceTypeRecordDBFInfo(file);
					reader = new SzDbfReader<ServiceTypeRecord, ServiceTypeRecordDBFInfo>(dbfInfo, file);
					ServiceTypeRecord record;
					while ((record = (ServiceTypeRecord) reader.read()) != null) {
						record.setSzFile(szFile);
						serviceTypeRecordService.create(record);
					}
				}
			} catch (IOException e) {
				log.warn("Can't read record from file with name {}", szFile.getUploadedFile().getOriginalName());
			} finally {
				if (reader != null) {
					reader.close();
				}
			}

			FPFileStatus status = fpFileService.getStatusByCodeAndModule(SzFile.PROCESSED_FILE_STATUS, moduleName);
			szFile.setStatus(status);
			szFileService.update(szFile);

			pLogger.info("Loading to DB szFile with id = {} finished", szFile.getId());
		}

		log.debug("Process szFile load to DB for fileIds = {} finished", fileIds);

		return RESULT_NEXT;
	}

	@Required
	public void setCharacteristicRecordService(RecordService<CharacteristicRecord> characteristicRecordService) {
		this.characteristicRecordService = characteristicRecordService;
	}

	@Required
	public void setServiceTypeRecordService(RecordService<ServiceTypeRecord> serviceTypeRecordService) {
		this.serviceTypeRecordService = serviceTypeRecordService;
	}

	@Required
	public void setSubsidyRecordService(RecordService<SubsidyRecord> subsidyRecordService) {
		this.subsidyRecordService = subsidyRecordService;
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
