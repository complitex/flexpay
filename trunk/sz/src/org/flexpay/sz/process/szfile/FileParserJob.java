package org.flexpay.sz.process.szfile;

import com.linuxense.javadbf.DBFException;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFileStatus;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
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
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Map;

public class FileParserJob extends Job {

	private String moduleName;
	private RecordService<CharacteristicRecord> characteristicRecordService;
	private RecordService<ServiceTypeRecord> serviceTypeRecordService;
	private RecordService<SubsidyRecord> subsidyRecordService;
	private SzFileService szFileService;
	private FPFileService fpFileService;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		Long fileId = (Long) parameters.get("fileId");

		SzFile szFile = szFileService.readFull(fileId);

		log.debug("Process szFile parser for fileId = {} started", fileId);

		if (szFile == null) {
			log.warn("Invalid File Id");
			return RESULT_ERROR;
		}

		try {
			SzFileUtil.deleteRecords(szFile);
		} catch (NotSupportedOperationException e) {
			log.warn("Can't delete records for file with id = {}", fileId);
			return RESULT_ERROR;
		}

		File file = FPFileUtil.getFileOnServer(szFile.getUploadedFile());
		Long szFileTypeCode = szFile.getType().getCode();

		SzDbfReader reader = null;

		FPFileStatus status = fpFileService.getStatusByCodeAndModule(SzFile.PROCESSED_FILE_STATUS, moduleName);
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
		} catch (DBFException e) {
			log.warn("Can't read record from file with name {}", szFile.getUploadedFile().getOriginalName());
			return RESULT_ERROR;
		} catch (FileNotFoundException e) {
			log.warn("Can't find file with name {}", szFile.getUploadedFile().getOriginalName());
			return RESULT_ERROR;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		szFile.setStatus(status);
		szFileService.update(szFile);

		log.debug("Process szFile parser for fileId = {} finished", fileId);

		return RESULT_NEXT;
	}

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
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
