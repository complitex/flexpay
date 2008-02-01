package org.flexpay.sz.convert;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.sz.dbf.CharacteristicDBFInfo;
import org.flexpay.sz.dbf.SubsidyRecordDBFInfo;
import org.flexpay.sz.dbf.SzDbfReader;
import org.flexpay.sz.dbf.SzDbfWriter;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.persistence.SubsidyRecord;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.persistence.SzFileType;
import org.flexpay.sz.service.RecordService;
import org.flexpay.sz.service.SzFileService;
import org.flexpay.sz.service.SzFileTypeService;

import com.linuxense.javadbf.DBFException;

public class SzFileUtil {

	private static RecordService<CharacteristicRecord> characteristicRecordService;
	private static RecordService<SubsidyRecord> subsidyRecordService;

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
			File responseFile = szFile.getResponseFile(ApplicationConfig
					.getInstance().getSzDataRoot());
			responseFile.delete();
		}
		
		// delete request file
		File requestFile = szFile.getRequestFile(ApplicationConfig
				.getInstance().getSzDataRoot());
		requestFile.delete();

		// delete SzFile
		szFileService.delete(szFile);
	}

	public static void loadToDb(SzFile szFile) throws FileNotFoundException,
			DBFException, NotSupportedOperationException {

		deleteRecords(szFile);

		File file = szFile.getRequestFile(ApplicationConfig.getInstance()
				.getSzDataRoot());
		SzFileType szFileType = szFile.getSzFileType();
		if (szFileType.getId().equals(SzFileTypeService.CHARACTERISTIC)) {
			CharacteristicDBFInfo dbfInfo = new CharacteristicDBFInfo(file);
			SzDbfReader<CharacteristicRecord, CharacteristicDBFInfo> reader = new SzDbfReader<CharacteristicRecord, CharacteristicDBFInfo>(
					dbfInfo, file);
			try {

				CharacteristicRecord record = null;
				while ((record = reader.read()) != null) {
					record.setSzFile(szFile);
					characteristicRecordService.create(record);
				}
			} finally {
				reader.close();
			}
		} else if (szFileType.getId().equals(SzFileTypeService.SUBSIDY)) {
			SubsidyRecordDBFInfo dbfInfo = new SubsidyRecordDBFInfo(file);
			SzDbfReader<SubsidyRecord, SubsidyRecordDBFInfo> reader = new SzDbfReader<SubsidyRecord, SubsidyRecordDBFInfo>(
					dbfInfo, file);
			try {

				SubsidyRecord record = null;
				while ((record = reader.read()) != null) {
					record.setSzFile(szFile);
					subsidyRecordService.create(record);
				}
			} finally {
				reader.close();
			}
		} else {
			throw new NotSupportedOperationException();
		}
	}

	public static void loadFromDb(SzFile szFile) throws Throwable {
		File oldInternalResponseFile = szFile.getInternalResponseFileName() == null ? null
				: szFile.getResponseFile(ApplicationConfig.getInstance()
						.getSzDataRoot());

		String internalResponseFileName = SzFile.getRandomString();
		szFile.setInternalResponseFileName(internalResponseFileName);
		File targetFile = szFile.getResponseFile(ApplicationConfig
				.getInstance().getSzDataRoot());

		SzFileType szFileType = szFile.getSzFileType();

		try {
			if (szFileType.getId().equals(SzFileTypeService.CHARACTERISTIC)) {
				List<CharacteristicRecord> recordList = null;

				File internalRequestFile = szFile
						.getRequestFile(ApplicationConfig.getInstance()
								.getSzDataRoot());
				CharacteristicDBFInfo dbfInfo = new CharacteristicDBFInfo(
						internalRequestFile);
				SzDbfWriter<CharacteristicRecord, CharacteristicDBFInfo> writer = null;

				try {
					writer = new SzDbfWriter<CharacteristicRecord, CharacteristicDBFInfo>(
							dbfInfo, targetFile, ApplicationConfig
									.getInstance()
									.getSzDefaultDbfFileEncoding(), true);

					Page<CharacteristicRecord> pager = new Page<CharacteristicRecord>();
					pager.setPageSize(100);

					while (!(recordList = characteristicRecordService
							.findObjects(pager, szFile.getId())).isEmpty()) {
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

				szFileService.update(szFile);
				if (oldInternalResponseFile != null) {
					oldInternalResponseFile.delete();
				}
			} else if (szFileType.getId().equals(SzFileTypeService.SUBSIDY)) {
				List<SubsidyRecord> recordList = null;

				File internalRequestFile = szFile
						.getRequestFile(ApplicationConfig.getInstance()
								.getSzDataRoot());
				SubsidyRecordDBFInfo dbfInfo = new SubsidyRecordDBFInfo(
						internalRequestFile);
				SzDbfWriter<SubsidyRecord, SubsidyRecordDBFInfo> writer = null;

				try {
					writer = new SzDbfWriter<SubsidyRecord, SubsidyRecordDBFInfo>(
							dbfInfo, targetFile, ApplicationConfig
									.getInstance()
									.getSzDefaultDbfFileEncoding(), true);

					Page<SubsidyRecord> pager = new Page<SubsidyRecord>();
					pager.setPageSize(100);

					while (!(recordList = subsidyRecordService.findObjects(
							pager, szFile.getId())).isEmpty()) {
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

				szFileService.update(szFile);
				if (oldInternalResponseFile != null) {
					oldInternalResponseFile.delete();
				}
			} else {
				throw new NotSupportedOperationException();
			}
		} catch (Throwable t) {
			szFile.setInternalResponseFileName(null);
			targetFile.delete();
			throw t;
		}

	}

	public static Integer getNumberOfRecord(SzFile szFile)
			throws NotSupportedOperationException {
		Long szFileTypeId = szFile.getSzFileType().getId();
		if (szFileTypeId.equals(SzFileTypeService.CHARACTERISTIC)) {
			Page<CharacteristicRecord> pager = new Page<CharacteristicRecord>();
			pager.setPageSize(1);
			characteristicRecordService.findObjects(pager, szFile.getId());
			return pager.getTotalNumberOfElements();
		} else if (szFileTypeId.equals(SzFileTypeService.SUBSIDY)) {
			Page<SubsidyRecord> pager = new Page<SubsidyRecord>();
			pager.setPageSize(1);
			subsidyRecordService.findObjects(pager, szFile.getId());
			return pager.getTotalNumberOfElements();
		} else {
			throw new NotSupportedOperationException();
		}
	}

	public static boolean isLoadedToDb(SzFile szFile)
			throws NotSupportedOperationException {
		return 0 < getNumberOfRecord(szFile);
	}

	public static void deleteRecords(SzFile szFile)
			throws NotSupportedOperationException {
		Long szFileTypeId = szFile.getSzFileType().getId();
		if (szFileTypeId.equals(SzFileTypeService.CHARACTERISTIC)) {
			characteristicRecordService.deleteBySzFileId(szFile.getId());
		} else if (szFileTypeId.equals(SzFileTypeService.SUBSIDY)) {
			subsidyRecordService.deleteBySzFileId(szFile.getId());
		} else {
			throw new NotSupportedOperationException();
		}
	}

	public void setSzFileService(SzFileService szFileService) {
		SzFileUtil.szFileService = szFileService;
	}

	public void setCharacteristicRecordService(
			RecordService<CharacteristicRecord> characteristicRecordService) {
		SzFileUtil.characteristicRecordService = characteristicRecordService;
	}

	public void setSubsidyRecordService(
			RecordService<SubsidyRecord> subsidyRecordService) {
		SzFileUtil.subsidyRecordService = subsidyRecordService;
	}

}
