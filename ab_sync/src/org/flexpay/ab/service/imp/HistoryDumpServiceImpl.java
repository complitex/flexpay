package org.flexpay.ab.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.ab.dao.HistoryDao;
import org.flexpay.ab.dao.HistorySourceDao;
import org.flexpay.ab.dao.UpdateConfigDao;
import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.ab.persistence.UpdateConfig;
import org.flexpay.ab.service.HistoryDumpService;
import org.flexpay.common.locking.LockManager;

import java.util.Iterator;

public class HistoryDumpServiceImpl implements HistoryDumpService {

	private Logger log = Logger.getLogger(getClass());

	private UpdateConfigDao updateConfigDao;
	private HistoryDao historyDao;
	private HistorySourceDao historySourceDao;
	private LockManager lockManager;

	/**
	 * Dump history from external source
	 */
	public void dumpHistory() {

		if (!lockManager.lock("sync_ab_lock")) {
			log.info("Another process has already requested a lock and is working");
			return;
		}

		long nRecords = 0;
		try {
			UpdateConfig config = updateConfigDao.getConfig();
			if (log.isInfoEnabled()) {
				log.info("Last dumped record was: " + config.getLastDumpedRecordId());
			}

				Iterator<HistoryRecord> it = historySourceDao.getRecords(config.getLastDumpedRecordId());
				while (it.hasNext()) {
					HistoryRecord record = it.next();
					historyDao.addRecord(record);
					++nRecords;
					config.setLastDumpedRecordId(record.getRecordId());
				}

				updateConfigDao.saveConfig(config);
		} catch (Exception e) {
			throw new RuntimeException("Failed dumping history", e);
		} finally {
			// release a lock
			lockManager.releaseLock("sync_ab_lock");

			// and close histroy source
			historySourceDao.close();
		}

		log.info("Dumped " + nRecords + " records.");
	}

	public void setUpdateConfigDao(UpdateConfigDao updateConfigDao) {
		this.updateConfigDao = updateConfigDao;
	}

	public void setHistoryDao(HistoryDao historyDao) {
		this.historyDao = historyDao;
	}

	public void setHistorySourceDao(HistorySourceDao historySourceDao) {
		this.historySourceDao = historySourceDao;
	}

	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}
}
