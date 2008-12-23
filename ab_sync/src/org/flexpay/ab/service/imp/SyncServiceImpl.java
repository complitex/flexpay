package org.flexpay.ab.service.imp;

import org.flexpay.ab.dao.HistoryDao;
import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.ab.persistence.ObjectType;
import org.flexpay.ab.service.SyncService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SyncServiceImpl implements SyncService {

	@NonNls
	private org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

	private HistoryDao historyDao;
	private CorrectionsService correctionsService;
	private DataSourceDescription sd;

	private TownProcessor townProcessor;
	private BuildingProcessor buildingProcessor;
	private ApartmentProcessor apartmentProcessor;
	private StreetProcessor streetProcessor;
	private DistrictProcessor districtProcessor;
	private StreetTypeProcessor streetTypeProcessor;
	private PersonProcessor personProcessor;

	private LockManager lockManager;

	private Long prevId = -1L;
	private ObjectType prevType = ObjectType.Unknown;
	@Nullable
	private DomainObject prevObj = null;
	private AbstractProcessor processor = null;
	private List<HistoryRecord> recordBuffer = new ArrayList<HistoryRecord>();

	/**
	 * Synchronize Address Bureau
	 */
	public void syncAB() {

		if (!lockManager.lock("sync_ab_lock")) {
			log.debug("Another process has already requested a lock and is working");
			return;
		}

		long timeStart = System.currentTimeMillis();

		try {
			prevId = -1L;
			prevType = ObjectType.Unknown;
			prevObj = null;
			processor = null;
			recordBuffer = new ArrayList<HistoryRecord>();

			int count = 0;

			while (true) {
				try {
					log.debug("Starting sync for next records");
					long time = System.currentTimeMillis();
					List<HistoryRecord> records = historyDao.getRecords(new Page(50000, 1));
					log.error("time spent for fetch: " + (System.currentTimeMillis() - time));
					if (records.isEmpty() || recordBuffer.containsAll(records)) {
						saveObject();
						log.debug("No more records.");
						break;
					}

					for (HistoryRecord record : records) {
						if (!prevId.equals(record.getObjectId()) || prevType != record.getObjectType()) {
							saveObject();
							prevId = record.getObjectId();
							prevType = record.getObjectType();
						}
						if (log.isDebugEnabled()) {
							log.debug("Sync record: " + record);
						}
						processRecord(record);
						recordBuffer.add(record);
						++count;
					}
				} catch (Exception e) {
					log.error("failed processing record", e);
					break;
				}
			}

			if (log.isInfoEnabled()) {
				log.info("Processed history records: " + count);
				log.info("History processing took " + (System.currentTimeMillis() - timeStart) + "ms");
			}

		} finally {
			lockManager.releaseLock("sync_ab_lock");
		}
	}

	private void saveObject() throws Exception {
		if (prevObj != null) {
			processor.saveObject(prevObj, String.valueOf(prevId), sd, correctionsService);
		}
		if (log.isDebugEnabled()) {
			log.debug("Marking as processed " + recordBuffer.size() + " history records.");
		}
		historyDao.setProcessed(recordBuffer);
		recordBuffer.clear();
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
			case Person:
				processor = personProcessor;
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

	public void setPersonProcessor(PersonProcessor personProcessor) {
		this.personProcessor = personProcessor;
	}

	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	public void setSd(DataSourceDescription sd) {
		this.sd = sd;
	}
}
