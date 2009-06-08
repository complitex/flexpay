package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.HistoryRec;

import java.util.Iterator;

public interface HistorySourceDao {

	/**
	 * Get list of history records
	 *
	 * @param lastRecord Last record obtained
	 * @return List of new records
	 * @throws Exception if failure occurs
	 */
	Iterator<HistoryRec> getRecords(Long lastRecord) throws Exception;

	/**
	 * Close source
	 */
	void close();
}
