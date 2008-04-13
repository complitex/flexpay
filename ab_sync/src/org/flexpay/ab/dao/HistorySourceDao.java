package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.HistoryRecord;

import java.util.List;

public interface HistorySourceDao {

	/**
	 * Get list of history records
	 *
	 * @param lastRecord Last record obtained
	 * @return List of new records
	 */
	List<HistoryRecord> getRecords(Long lastRecord);
}
