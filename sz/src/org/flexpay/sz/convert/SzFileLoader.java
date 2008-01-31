package org.flexpay.sz.convert;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.sz.dbf.CharacteristicDBFInfo;
import org.flexpay.sz.dbf.SzDbfReader;
import org.flexpay.sz.dbf.SzDbfWriter;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.RecordService;
import org.flexpay.sz.service.SzFileService;

import com.linuxense.javadbf.DBFException;

public class SzFileLoader {

	RecordService<CharacteristicRecord> recordService;

	SzFileService szFileService;

	public void loadToDb(SzFile szFile) throws FileNotFoundException,
			DBFException {
		// TODO delete all records for this importFile from
		// DB(characteristics,...)

		File file = szFile.getRequestFile(ApplicationConfig.getInstance()
				.getSzDataRoot());
		CharacteristicDBFInfo dbfInfo = new CharacteristicDBFInfo(file);
		SzDbfReader<CharacteristicRecord, CharacteristicDBFInfo> reader = new SzDbfReader<CharacteristicRecord, CharacteristicDBFInfo>(
				dbfInfo, file);
		try {

			CharacteristicRecord characteristic = null;
			while ((characteristic = reader.read()) != null) {
				characteristic.setSzFile(szFile);
				recordService.create(characteristic);
			}
		} finally {
			reader.close();
		}

	}

	public void loadFromDb(SzFile szFile) throws Throwable {
		File oldInternalResponseFile = szFile.getInternalResponseFileName() == null ? null
				: szFile.getResponseFile(ApplicationConfig.getInstance()
						.getSzDataRoot());

		String internalResponseFileName = SzFile.getRandomString();
		szFile.setInternalResponseFileName(internalResponseFileName);
		File targetFile = szFile.getResponseFile(ApplicationConfig
				.getInstance().getSzDataRoot());

		try {
			List<CharacteristicRecord> recordList = null;

			File internalRequestFile = szFile.getRequestFile(ApplicationConfig
					.getInstance().getSzDataRoot());
			CharacteristicDBFInfo dbfInfo = new CharacteristicDBFInfo(
					internalRequestFile);
			SzDbfWriter<CharacteristicRecord, CharacteristicDBFInfo> writer = null;

			try {
				writer = new SzDbfWriter<CharacteristicRecord, CharacteristicDBFInfo>(
						dbfInfo, targetFile, ApplicationConfig.getInstance()
								.getSzDefaultDbfFileEncoding(), true);

				Page<CharacteristicRecord> pager = new Page<CharacteristicRecord>();
				pager.setPageSize(100);

				while (!(recordList = recordService.findObjects(pager, szFile
						.getId())).isEmpty()) {
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
			oldInternalResponseFile.delete();
		} catch (Throwable t) {
			szFile.setInternalResponseFileName(null);
			targetFile.delete();
			throw t;
		}

	}

	public void setRecordService(
			RecordService<CharacteristicRecord> recordService) {
		this.recordService = recordService;
	}

	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

}
