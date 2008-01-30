package org.flexpay.sz.convert;

import java.io.File;
import java.io.FileNotFoundException;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.sz.dbf.CharacteristicDBFInfo;
import org.flexpay.sz.dbf.SzDbfReader;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.RecordService;

import com.linuxense.javadbf.DBFException;

public class SzFileLoader {

	RecordService<CharacteristicRecord> recordService;

	public void loadToDb(SzFile szFile) throws FileNotFoundException,
			DBFException {
		// TODO delete all records for this importFile from
		// DB(characteristics,...)
		
		Page<CharacteristicRecord> pager = new Page<CharacteristicRecord>(10, 2);
		recordService.findObjects(pager, szFile.getId());
		

		File file = szFile.getFile(ApplicationConfig.getInstance()
				.getSzDataRoot());
		/*Cha racteristicDBFInfo dbfInfo = new CharacteristicDBFInfo(file);
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
		}*/

	}
	
	public void loadFromDb(SzFile importFile){
		
		// 
		
		
	}

	public void setRecordService(RecordService<CharacteristicRecord> recordService) {
		this.recordService = recordService;
	}

}
