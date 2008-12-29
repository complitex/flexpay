package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.HistoryRecord;

import java.util.List;
import java.util.Iterator;

public interface HistorySourceDao {

	/**
	 * Get list of history records
	 *
	 * @param lastRecord Last record obtained
	 * @return List of new records
	 * @throws Exception if failure occurs
	 */
	Iterator<HistoryRecord> getRecords(Long lastRecord) throws Exception;

	/**
	 * Close source
	 */
	void close();
}
