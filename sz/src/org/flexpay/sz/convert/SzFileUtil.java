package org.flexpay.sz.convert;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.sz.dbf.*;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.persistence.ServiceTypeRecord;
import org.flexpay.sz.persistence.SubsidyRecord;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.RecordService;
import org.flexpay.sz.service.SzFileService;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.util.List;

public class SzFileUtil {

	@NonNls
	protected static Logger log = LoggerFactory.getLogger(SzFileUtil.class);

	private static RecordService<CharacteristicRecord> characteristicRecordService;
	private static RecordService<SubsidyRecord> subsidyRecordService;
	private static RecordService<ServiceTypeRecord> serviceTypeRecordService;

	private static FPFileService fpFileService;
	private static SzFileService szFileService;

	@SuppressWarnings ({"unchecked"})
	public static void loadFromDbGeneric(SzFile szFile, File targetFile, DBFInfo dbfInfo, RecordService recordService) throws Exception {

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

	public static void loadFromDb(SzFile szFile) throws Exception {

		FPFile fileToDownload = szFile.getFileToDownload();
		FPFile uploadedFile = szFile.getUploadedFile();
		FPModule module = uploadedFile.getModule();

		if (fileToDownload != null) {
			szFile.setFileToDownload(null);
			szFile.setStatus(fpFileService.getStatusByCodeAndModule(SzFile.PROCESSING_FILE_STATUS, module.getName()));
			szFileService.update(szFile);
			fpFileService.delete(fileToDownload);
		}

		fileToDownload = new FPFile();
		fileToDownload.setUserName(szFile.getUserName());
		fileToDownload.setOriginalName(uploadedFile.getOriginalName());
		fileToDownload.setModule(module);
		szFile.setFileToDownload(fileToDownload);

		File targetFile = FPFileUtil.saveToFileSystem(fileToDownload, new File(uploadedFile.getOriginalName()));
		fileToDownload.setNameOnServer(targetFile.getName());

		Long szFileTypeCode = szFile.getType().getCode();
		File uploadedFileOnSystem = FPFileUtil.getFileOnServer(uploadedFile);

		try {
			if (SzFile.CHARACTERISTIC_FILE_TYPE.equals(szFileTypeCode)) {
				DBFInfo dbfInfo = new CharacteristicDBFInfo(uploadedFileOnSystem);
				loadFromDbGeneric(szFile, targetFile, dbfInfo, characteristicRecordService);
			} else if (SzFile.SUBSIDY_FILE_TYPE.equals(szFileTypeCode)) {
				DBFInfo dbfInfo = new SubsidyRecordDBFInfo(uploadedFileOnSystem);
				loadFromDbGeneric(szFile, targetFile, dbfInfo, subsidyRecordService);
			} else if (SzFile.SRV_TYPES_FILE_TYPE.equals(szFileTypeCode)) {
				DBFInfo dbfInfo = new ServiceTypeRecordDBFInfo(uploadedFileOnSystem);
				loadFromDbGeneric(szFile, targetFile, dbfInfo, serviceTypeRecordService);
			} else {
				throw new NotSupportedOperationException();
			}

			fileToDownload.setSize(targetFile.length());
			szFile.setStatus(fpFileService.getStatusByCodeAndModule(SzFile.PROCESSED_FILE_STATUS, module.getName()));
			szFileService.update(szFile);
		} catch (Exception e) {
			log.error("Error loading data fro DB and creating file to download", e);
			szFile.setStatus(fpFileService.getStatusByCodeAndModule(SzFile.PROCESSED_WITH_ERRORS_FILE_STATUS, module.getName()));
			szFile.setFileToDownload(null);
			szFileService.update(szFile);
			targetFile.delete();
			throw e;
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
