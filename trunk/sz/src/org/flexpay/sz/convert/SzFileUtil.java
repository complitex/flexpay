package org.flexpay.sz.convert;

import com.linuxense.javadbf.DBFException;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.sz.dbf.*;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.persistence.ServiceTypeRecord;
import org.flexpay.sz.persistence.SubsidyRecord;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.RecordService;
import org.flexpay.sz.service.SzFileService;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

public class SzFileUtil {

	private static RecordService<CharacteristicRecord> characteristicRecordService;
	private static RecordService<SubsidyRecord> subsidyRecordService;
	private static RecordService<ServiceTypeRecord> serviceTypeRecordService;

	private static SzFileService szFileService;

	public static void delete(SzFile szFile) {
		// delete records
		try {
			deleteRecords(szFile);
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
	}

	public static void loadToDb(SzFile szFile) throws FileNotFoundException,
													  DBFException, NotSupportedOperationException {

		deleteRecords(szFile);

		File file = FPFileUtil.getFileOnServer(szFile.getUploadedFile());
		Long szFileTypeCode = szFile.getType().getCode();
		if (SzFile.CHARACTERISTIC_FILE_TYPE.equals(szFileTypeCode)) {
			CharacteristicDBFInfo dbfInfo = new CharacteristicDBFInfo(file);
			SzDbfReader<CharacteristicRecord, CharacteristicDBFInfo> reader =
					new SzDbfReader<CharacteristicRecord, CharacteristicDBFInfo>(dbfInfo, file);
			try {
				CharacteristicRecord record;
				while ((record = reader.read()) != null) {
					record.setSzFile(szFile);
					characteristicRecordService.create(record);
				}
			} finally {
				reader.close();
			}
		} else if (SzFile.SUBSIDY_FILE_TYPE.equals(szFileTypeCode)) {
			SubsidyRecordDBFInfo dbfInfo = new SubsidyRecordDBFInfo(file);
			SzDbfReader<SubsidyRecord, SubsidyRecordDBFInfo> reader =
					new SzDbfReader<SubsidyRecord, SubsidyRecordDBFInfo>(dbfInfo, file);
			try {
				SubsidyRecord record;
				while ((record = reader.read()) != null) {
					record.setSzFile(szFile);
					subsidyRecordService.create(record);
				}
			} finally {
				reader.close();
			}
		} else if (SzFile.SRV_TYPES_FILE_TYPE.equals(szFileTypeCode)) {
			ServiceTypeRecordDBFInfo dbfInfo = new ServiceTypeRecordDBFInfo(file);
			SzDbfReader<ServiceTypeRecord, ServiceTypeRecordDBFInfo> reader =
					new SzDbfReader<ServiceTypeRecord, ServiceTypeRecordDBFInfo>(dbfInfo, file);
			try {
				ServiceTypeRecord record;
				while ((record = reader.read()) != null) {
					record.setSzFile(szFile);
					serviceTypeRecordService.create(record);
				}
			} finally {
				reader.close();
			}
		} else {
			throw new NotSupportedOperationException();
		}
	}

	@SuppressWarnings ({"unchecked"})
	public static void loadFromDbGeneric(
			SzFile szFile, File targetFile, DBFInfo dbfInfo, RecordService recordService)
			throws Exception {

		SzDbfWriter writer = new SzDbfWriter(dbfInfo, targetFile, getDbfEncoding(), true);

		try {
			Page pager = new Page(100, 1);

			List recordList;
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

		File oldFileToDownload = FPFileUtil.getFileOnServer(szFile.getFileToDownload());
		FPFile newFileToDownload;
		if (oldFileToDownload != null) {
			oldFileToDownload.delete();
			szFile.getFileToDownload().setCreationDate(new Date());
		} else {
			newFileToDownload = new FPFile();
			newFileToDownload.setUserName(szFile.getUserName());
			newFileToDownload.setOriginalName(szFile.getUploadedFile().getOriginalName());
			newFileToDownload.setModule(szFile.getUploadedFile().getModule());
			szFile.setFileToDownload(newFileToDownload);
		}
		File targetFile = FPFileUtil.saveToFileSystem(szFile.getFileToDownload(), new File(szFile.getUploadedFile().getOriginalName()));

		Long szFileTypeCode = szFile.getType().getCode();
		File uploadedFile = FPFileUtil.getFileOnServer(szFile.getUploadedFile());

		try {
			if (SzFile.CHARACTERISTIC_FILE_TYPE.equals(szFileTypeCode)) {
				DBFInfo dbfInfo = new CharacteristicDBFInfo(uploadedFile);
				loadFromDbGeneric(szFile, targetFile, dbfInfo, characteristicRecordService);
			} else if (SzFile.SUBSIDY_FILE_TYPE.equals(szFileTypeCode)) {
				DBFInfo dbfInfo = new SubsidyRecordDBFInfo(uploadedFile);
				loadFromDbGeneric(szFile, targetFile, dbfInfo, subsidyRecordService);
			} else if (SzFile.SRV_TYPES_FILE_TYPE.equals(szFileTypeCode)) {
				DBFInfo dbfInfo = new ServiceTypeRecordDBFInfo(uploadedFile);
				loadFromDbGeneric(szFile, targetFile, dbfInfo, serviceTypeRecordService);
			} else {
				throw new NotSupportedOperationException();
			}

			szFile.getFileToDownload().setNameOnServer(targetFile.getName());
			szFile.getFileToDownload().setSize(targetFile.length());
			szFileService.update(szFile);
		} catch (Exception t) {
			szFile.setFileToDownload(null);
			targetFile.delete();
			throw t;
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
	}

	@Required
	public void setSzFileService(SzFileService szFileService) {
		SzFileUtil.szFileService = szFileService;
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
