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
	 * @param lastModifiedDate Date to filter records
	 * @return List of HistoryRecord instances
	 */
	List<HistoryRecord> getRecords(Page pager, Date lastModifiedDate);
}
