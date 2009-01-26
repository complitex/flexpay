package org.flexpay.sz.process.szfile;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.FPFileStatus;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.sz.convert.NotSupportedOperationException;
import org.flexpay.sz.convert.SzFileUtil;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.persistence.ServiceTypeRecord;
import org.flexpay.sz.persistence.SubsidyRecord;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.RecordService;
import org.flexpay.sz.service.SzFileService;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class SzFileDeleteFromDbJob extends Job {

	private RecordService<CharacteristicRecord> characteristicRecordService;
	private RecordService<SubsidyRecord> subsidyRecordService;
	private RecordService<ServiceTypeRecord> serviceTypeRecordService;
	private SzFileService szFileService;
	private FPFileService fpFileService;

	@SuppressWarnings({"unchecked"})
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		Set<Long> fileIds = (Set<Long>) parameters.get("fileIds");

		log.debug("Process szFile delete from DB for fileIds = {} started", fileIds);

		Collection<SzFile> szFiles = szFileService.listSzFilesByIds(fileIds);

		if (szFiles == null || szFiles.isEmpty()) {
			log.warn("Invalid File Ids");
			return RESULT_ERROR;
		}

		for (SzFile szFile : szFiles) {

			log.debug("Deleting from DB szFile with id = {} started", szFile.getId());

			try {
				if (!SzFileUtil.isLoadedToDb(szFile)) {
					log.debug("SzFile with id {} not loaded to DB", szFile.getId());
					continue;
				}
			} catch (NotSupportedOperationException e) {
				// do nothing
			}

			FPFile file = szFile.getFileToDownload();
			try {
				Long szFileTypeCode = szFile.getType().getCode();
				if (SzFile.CHARACTERISTIC_FILE_TYPE.equals(szFileTypeCode)) {
					characteristicRecordService.deleteBySzFileId(szFile.getId());
				} else if (SzFile.SUBSIDY_FILE_TYPE.equals(szFileTypeCode)) {
					subsidyRecordService.deleteBySzFileId(szFile.getId());
				} else if (SzFile.SRV_TYPES_FILE_TYPE.equals(szFileTypeCode)) {
					serviceTypeRecordService.deleteBySzFileId(szFile.getId());
				} else {
					throw new NotSupportedOperationException();
				}

				if (file != null) {
					szFile.setFileToDownload(null);
					szFileService.update(szFile);
					fpFileService.delete(file);
				}
			} catch (FlexPayException e) {
				log.error("Error deleting file with id=" + file.getId(), e);
			} catch (NotSupportedOperationException e) {
				log.error("Can't delete recors from DB for szFile with id=" + szFile.getId(), e);
			}

			FPFileStatus status = fpFileService.getStatusByCodeAndModule(SzFile.PROCESSED_FILE_STATUS, file.getModule().getName());
			szFile.setStatus(status);
			szFileService.update(szFile);

			log.debug("Deleting from DB szFile with id = {} finished", szFile.getId());
		}

		log.debug("Process szFile delete from DB for fileIds = {} finished", fileIds);

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
