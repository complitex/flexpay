package org.flexpay.ab.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.ab.dao.HistoryDao;
import org.flexpay.ab.dao.UpdateConfigDao;
import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.ab.persistence.UpdateConfig;
import org.flexpay.ab.persistence.ObjectType;
import org.flexpay.ab.service.SyncService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.DateIntervalUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = false, rollbackFor = Exception.class)
public class SyncServiceImpl implements SyncService {

	private static Logger log = Logger.getLogger(SyncServiceImpl.class);

	private HistoryDao historyDao;
	private UpdateConfigDao updateConfigDao;
	private CorrectionsService correctionsService;
	private DataSourceDescription sd;

	private TownProcessor townProcessor;
	private BuildingProcessor buildingProcessor;
	private ApartmentProcessor apartmentProcessor;
	private StreetProcessor streetProcessor;
	private DistrictProcessor districtProcessor;
	private StreetTypeProcessor streetTypeProcessor;

	private Long prevId = -1L;
	private ObjectType prevType = ObjectType.Unknown;
	private DomainObject prevObj = null;
	private AbstractProcessor processor = null;

	/**
	 * Synchronize Address Bureau
	 */
	public void syncAB() {
		UpdateConfig config = updateConfigDao.getConfig();
		config.setLastUpdateDate(DateIntervalUtil.now());

		prevId = -1L;
		prevType = ObjectType.Unknown;
		prevObj = null;
		processor = null;

		while (true) {
			log.info("Starting sync for next records");
			List<HistoryRecord> records = historyDao.getRecords(new Page(10, 1), config.getLastRecordUpdateTime());
			if (records.isEmpty()) {
				saveObject();
				log.info("No more records.");
				break;
			}

			try {
				for (HistoryRecord record : records) {
					if (!prevId.equals(record.getObjectId()) || prevType != record.getObjectType()) {
						saveObject();
						prevId = record.getObjectId();
						prevType = record.getObjectType();
						config.setLastRecordUpdateTime(record.getRecordDate());
					}
					processRecord(record);
				}

				if (true) {
					log.info("Break!");
					break;
				}
			} catch (Exception e) {
				log.error("failed processing record", e);
				break;
			}
		}
	}

	private void saveObject() {
		if (prevObj != null ) {
			processor.saveObject(prevObj, String.valueOf(prevId), sd, correctionsService);
		}
		prevObj = null;
		prevId = -1L;
		processor = null;
		prevType = ObjectType.Unknown;
	}

	private void processRecord(HistoryRecord record) throws Exception {
		switch (record.getObjectType()) {
			case Apartment:
				processor = apartmentProcessor;
				break;
			case Building:
				processor = buildingProcessor;
				break;
			case Street:
				processor = streetProcessor;
				break;
			case StreetType:
				processor = streetTypeProcessor;
				break;
			case District:
				processor = districtProcessor;
				break;
			case Town:
				processor = townProcessor;
				break;
		}

		processAction(record);
	}

	private void processAction(HistoryRecord record) throws Exception {
		switch (record.getSyncAction()) {
			case Create:
				prevObj = processor.createObject(prevObj, String.valueOf(record.getObjectId()), sd, correctionsService);
				// fall through
			case Change:
				prevObj = processor.findObject(prevObj, String.valueOf(record.getObjectId()), sd, correctionsService);
				processor.setProperty(prevObj, record, sd, correctionsService);
				break;
			case Delete:
				prevObj = processor.findObject(prevObj, String.valueOf(record.getObjectId()), sd, correctionsService);
				processor.deleteObject(prevObj);
		}
	}

	public void setHistoryDao(HistoryDao historyDao) {
		this.historyDao = historyDao;
	}

	public void setUpdateConfigDao(UpdateConfigDao updateConfigDao) {
		this.updateConfigDao = updateConfigDao;
	}

	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	public void setTownProcessor(TownProcessor townProcessor) {
		this.townProcessor = townProcessor;
	}

	public void setBuildingProcessor(BuildingProcessor buildingProcessor) {
		this.buildingProcessor = buildingProcessor;
	}

	public void setApartmentProcessor(ApartmentProcessor apartmentProcessor) {
		this.apartmentProcessor = apartmentProcessor;
	}

	public void setStreetProcessor(StreetProcessor streetProcessor) {
		this.streetProcessor = streetProcessor;
	}

	public void setDistrictProcessor(DistrictProcessor districtProcessor) {
		this.districtProcessor = districtProcessor;
	}

	public void setStreetTypeProcessor(StreetTypeProcessor streetTypeProcessor) {
		this.streetTypeProcessor = streetTypeProcessor;
	}

	public void setSd(DataSourceDescription sd) {
		this.sd = sd;
	}
}
