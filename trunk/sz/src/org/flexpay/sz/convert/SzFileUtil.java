package org.flexpay.sz.convert;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.sz.dbf.DBFInfo;
import org.flexpay.sz.dbf.SzDbfWriter;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.persistence.ServiceTypeRecord;
import org.flexpay.sz.persistence.SubsidyRecord;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.RecordService;
import org.flexpay.sz.service.SzFileService;
import org.flexpay.sz.util.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.List;

public class SzFileUtil {

	protected static Logger log = LoggerFactory.getLogger(SzFileUtil.class);

	private static RecordService<CharacteristicRecord> characteristicRecordService;
	private static RecordService<SubsidyRecord> subsidyRecordService;
	private static RecordService<ServiceTypeRecord> serviceTypeRecordService;

	private static FPFileService fpFileService;
	private static SzFileService szFileService;

	@SuppressWarnings ({"unchecked"})
	public static void loadFromDbGeneric(SzFile szFile, FPFile targetFile, DBFInfo dbfInfo, RecordService recordService) throws IOException {

		SzDbfWriter writer = new SzDbfWriter(dbfInfo, targetFile, getDbfEncoding(), true);

		try {
			Page<Object> pager = new Page<Object>(100, 1);

			List<Object> recordList;
			while (!(recordList = recordService.findObjects(pager, szFile.getId())).isEmpty()) {
				writer.write(recordList);
				if (pager.hasNextPage()) {
					pager.setPageNumber(pager.getNextPageNumber());
				} else {
					break;
				}
			}
		} finally {
			writer.close();
		}

	}

	private static String getDbfEncoding() {
		return ApplicationConfig.getSzDefaultDbfFileEncoding();
	}

	public static Integer getNumberOfRecord(SzFile szFile) throws NotSupportedOperationException {

		Long szFileTypeCode = szFile.getType().getCode();
		if (SzFile.CHARACTERISTIC_FILE_TYPE.equals(szFileTypeCode)) {
			Page<CharacteristicRecord> pager = new Page<CharacteristicRecord>();
			pager.setPageSize(1);
			characteristicRecordService.findObjects(pager, szFile.getId());
			return pager.getTotalNumberOfElements();
		} else if (SzFile.SUBSIDY_FILE_TYPE.equals(szFileTypeCode)) {
			Page<SubsidyRecord> pager = new Page<SubsidyRecord>();
			pager.setPageSize(1);
			subsidyRecordService.findObjects(pager, szFile.getId());
			return pager.getTotalNumberOfElements();
		} else if (SzFile.SRV_TYPES_FILE_TYPE.equals(szFileTypeCode)) {
			Page<ServiceTypeRecord> pager = new Page<ServiceTypeRecord>();
			pager.setPageSize(1);
			serviceTypeRecordService.findObjects(pager, szFile.getId());
			return pager.getTotalNumberOfElements();
		} else {
			throw new NotSupportedOperationException();
		}
	}

	public static boolean isLoadedToDb(SzFile szFile) throws NotSupportedOperationException {
		return 0 < getNumberOfRecord(szFile);
	}

	public static void deleteRecords(SzFile szFile) throws NotSupportedOperationException {

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

		FPFile file = szFile.getFileToDownload();
		try {
			if (file != null) {
				szFile.setFileToDownload(null);
				szFileService.update(szFile);
				fpFileService.delete(file);
			}
		} catch (FlexPayException e) {
			log.error("Error deleting file with id " + file.getId(), e);
		}

	}

	@Required
	public void setSzFileService(SzFileService szFileService) {
		SzFileUtil.szFileService = szFileService;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		SzFileUtil.fpFileService = fpFileService;
	}

	@Required
	public void setCharacteristicRecordService(RecordService<CharacteristicRecord> characteristicRecordService) {
		SzFileUtil.characteristicRecordService = characteristicRecordService;
	}

	@Required
	public void setSubsidyRecordService(RecordService<SubsidyRecord> subsidyRecordService) {
		SzFileUtil.subsidyRecordService = subsidyRecordService;
	}

	@Required
	public void setServiceTypeRecordService(RecordService<ServiceTypeRecord> serviceTypeRecordService) {
		SzFileUtil.serviceTypeRecordService = serviceTypeRecordService;
	}

}
