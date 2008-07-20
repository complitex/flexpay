package org.flexpay.sz.convert;

import com.linuxense.javadbf.DBFException;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.sz.dbf.*;
import org.flexpay.sz.persistence.*;
import org.flexpay.sz.service.RecordService;
import org.flexpay.sz.service.SzFileService;
import org.flexpay.sz.service.SzFileTypeService;

import java.io.File;
import java.io.FileNotFoundException;
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
		if (szFile.getInternalResponseFileName() != null) {
			File responseFile = szFile.getResponseFile(getSzDataRoot());
			responseFile.delete();
		}

		// delete request file and parent dir if no more files in this dir
		File requestFile = szFile.getRequestFile(getSzDataRoot());
		File parentDir = requestFile.getParentFile();
		requestFile.delete();
		parentDir.delete();

		// delete SzFile
		szFileService.delete(szFile);
	}

	public static void loadToDb(SzFile szFile) throws FileNotFoundException,
													  DBFException, NotSupportedOperationException {

		deleteRecords(szFile);

		File file = szFile.getRequestFile(getSzDataRoot());
		SzFileType szFileType = szFile.getSzFileType();
		if (SzFileTypeService.CHARACTERISTIC.equals(szFileType.getId())) {
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
		} else if (SzFileTypeService.SUBSIDY.equals(szFileType.getId())) {
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
		} else if (SzFileTypeService.SRV_TYPES.equals(szFileType.getId())) {
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

		File oldInternalResponseFile = szFile.getInternalResponseFileName() == null
									   ? null : szFile.getResponseFile(getSzDataRoot());

		String internalResponseFileName = SzFile.getRandomString();
		szFile.setInternalResponseFileName(internalResponseFileName);
		File targetFile = szFile.getResponseFile(getSzDataRoot());

		SzFileType szFileType = szFile.getSzFileType();
		File internalRequestFile = szFile.getRequestFile(getSzDataRoot());

		try {
			if (SzFileTypeService.CHARACTERISTIC.equals(szFileType.getId())) {
				DBFInfo dbfInfo = new CharacteristicDBFInfo(internalRequestFile);
				loadFromDbGeneric(szFile, targetFile, dbfInfo, characteristicRecordService);
			} else if (SzFileTypeService.SUBSIDY.equals(szFileType.getId())) {
				DBFInfo dbfInfo = new SubsidyRecordDBFInfo(internalRequestFile);
				loadFromDbGeneric(szFile, targetFile, dbfInfo, subsidyRecordService);
			} else if (SzFileTypeService.SRV_TYPES.equals(szFileType.getId())) {
				DBFInfo dbfInfo = new ServiceTypeRecordDBFInfo(internalRequestFile);
				loadFromDbGeneric(szFile, targetFile, dbfInfo, serviceTypeRecordService);
			} else {
				throw new NotSupportedOperationException();
			}

			szFileService.update(szFile);
			if (oldInternalResponseFile != null) {
				oldInternalResponseFile.delete();
			}
		} catch (Exception t) {
			szFile.setInternalResponseFileName(null);
			targetFile.delete();
			throw t;
		}
	}

	private static String getDbfEncoding() {
		return ApplicationConfig.getSzDefaultDbfFileEncoding();
	}

	private static File getSzDataRoot() {
		return ApplicationConfig.getSzDataRoot();
	}

	public static Integer getNumberOfRecord(SzFile szFile)
			throws NotSupportedOperationException {

		Long szFileTypeId = szFile.getSzFileType().getId();
		if (SzFileTypeService.CHARACTERISTIC.equals(szFileTypeId)) {
			Page<CharacteristicRecord> pager = new Page<CharacteristicRecord>();
			pager.setPageSize(1);
			characteristicRecordService.findObjects(pager, szFile.getId());
			return pager.getTotalNumberOfElements();
		} else if (SzFileTypeService.SUBSIDY.equals(szFileTypeId)) {
			Page<SubsidyRecord> pager = new Page<SubsidyRecord>();
			pager.setPageSize(1);
			subsidyRecordService.findObjects(pager, szFile.getId());
			return pager.getTotalNumberOfElements();
		} else if (SzFileTypeService.SRV_TYPES.equals(szFileTypeId)) {
			Page<ServiceTypeRecord> pager = new Page<ServiceTypeRecord>();
			pager.setPageSize(1);
			serviceTypeRecordService.findObjects(pager, szFile.getId());
			return pager.getTotalNumberOfElements();
		} else {
			throw new NotSupportedOperationException();
		}
	}

	public static boolean isLoadedToDb(SzFile szFile)
			throws NotSupportedOperationException {
		return 0 < getNumberOfRecord(szFile);
	}

	public static void deleteRecords(SzFile szFile) throws NotSupportedOperationException {

		Long szFileTypeId = szFile.getSzFileType().getId();
		if (SzFileTypeService.CHARACTERISTIC.equals(szFileTypeId)) {
			characteristicRecordService.deleteBySzFileId(szFile.getId());
		} else if (SzFileTypeService.SUBSIDY.equals(szFileTypeId)) {
			subsidyRecordService.deleteBySzFileId(szFile.getId());
		} else if (SzFileTypeService.SRV_TYPES.equals(szFileTypeId)) {
			serviceTypeRecordService.deleteBySzFileId(szFile.getId());
		} else {
			throw new NotSupportedOperationException();
		}
	}

	public void setSzFileService(SzFileService szFileService) {
		SzFileUtil.szFileService = szFileService;
	}

	public void setCharacteristicRecordService(RecordService<CharacteristicRecord> characteristicRecordService) {
		SzFileUtil.characteristicRecordService = characteristicRecordService;
	}

	public void setSubsidyRecordService(RecordService<SubsidyRecord> subsidyRecordService) {
		SzFileUtil.subsidyRecordService = subsidyRecordService;
	}

	public void setServiceTypeRecordService(RecordService<ServiceTypeRecord> serviceTypeRecordService) {
		SzFileUtil.serviceTypeRecordService = serviceTypeRecordService;
	}
}
