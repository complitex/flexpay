package org.flexpay.ab.service.impl;

import org.flexpay.ab.dao.HistoryDao;
import org.flexpay.ab.persistence.HistoryRec;
import org.flexpay.ab.persistence.ObjectType;
import org.flexpay.ab.service.SyncSecurity;
import org.flexpay.ab.service.SyncService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;

public class SyncServiceImpl implements SyncService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private int fetchPageSize;
	private HistoryDao historyDao;
	private CorrectionsService correctionsService;
	private Stub<DataSourceDescription> sd;

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
	private AbstractProcessor<?> processor = null;
	private List<HistoryRec> recordBuffer = list();

	/**
	 * Synchronize Address Bureau
	 */
    @Override
	public void syncAB() {

		if (!lockManager.lock("sync_ab_lock")) {
			log.debug("Another process has already requested a lock and is working");
			return;
		}

		SyncSecurity.authenticateSyncer();

		long timeStart = System.currentTimeMillis();

		try {
			prevId = -1L;
			prevType = ObjectType.Unknown;
			prevObj = null;
			processor = null;
			recordBuffer = list();

			int count = 0;

			while (true) {
				try {
					log.debug("Starting sync for next records");
					long time = System.currentTimeMillis();
					List<HistoryRec> records = historyDao.getRecords(new Page(fetchPageSize, 1));
					if (log.isErrorEnabled()) {
						log.error("time spent for fetch: {}", (System.currentTimeMillis() - time));
					}
					if (records.isEmpty() || recordBuffer.containsAll(records)) {
						saveObject();
						log.debug("No more records.");
						break;
					}

					for (HistoryRec record : records) {
						if (!prevId.equals(record.getObjectId()) || prevType != record.getObjectType()) {
							saveObject();
							prevId = record.getObjectId();
							prevType = record.getObjectType();
						}
						log.debug("Sync record: {}", record);
						processRecord(record);
						recordBuffer.add(record);
						++count;
					}
				} catch (Exception e) {
					log.error("failed processing record", e);
					break;
				}
			}

			log.info("Processed history records: {}", count);
			if (log.isInfoEnabled()) {
				log.info("History processing took {} ms", (System.currentTimeMillis() - timeStart));
			}

		} finally {
			lockManager.releaseLock("sync_ab_lock");
		}
	}

	private void saveObject() throws Exception {
		if (prevObj != null) {
			processor.saveObject(prevObj, String.valueOf(prevId), sd, correctionsService);
		}
		log.debug("Marking as processed {} history records.", recordBuffer.size());
		historyDao.setProcessed(recordBuffer);
		recordBuffer.clear();
		prevObj = null;
		prevId = -1L;
		processor = null;
		prevType = ObjectType.Unknown;
	}

	private void processRecord(HistoryRec record) throws Exception {
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

	private void processAction(HistoryRec record) throws Exception {
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
		this.sd = stub(sd);
	}

	@Required
	public void setFetchPageSize(int fetchPageSize) {
		this.fetchPageSize = fetchPageSize;
	}
}
