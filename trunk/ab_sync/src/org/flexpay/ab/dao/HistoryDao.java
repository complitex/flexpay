package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.common.dao.paging.Page;

import java.util.List;
import java.util.Date;

public interface HistoryDao {

	/**
	 * List history records
	 *
	 * @param pager Page instance
	 * @return List of HistoryRecord instances
	 */
	List<HistoryRecord> getRecords(Page pager);

	/**
	 * Set records as processed
	 *
	 * @param records List of history records to mark as processed
	 */
	void setProcessed(List<HistoryRecord> records);

	/**
	 * Create a new history record
	 *
	 * @param record HistoryRecord
	 */
	void addRecord(HistoryRecord record);
}
