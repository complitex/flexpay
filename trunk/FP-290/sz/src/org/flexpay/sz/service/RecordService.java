package org.flexpay.sz.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.sz.persistence.Record;

import java.util.List;

public interface RecordService<R extends Record> {
	/**
	 * Create Record
	 *
	 * @param record Record
	 * @return created Record object
	 */
	R create(R record);

	/**
	 * Get all Record by SzFile in page mode
	 *
	 * @param pager Page object
	 * @return List of Record objects for pager
	 */
	List<R> findObjects(Page<R> pager, Long szFileId);

	/**
	 * Delete all Record by SzFile
	 *
	 * @param id Record's id field
	 */
	void deleteBySzFileId(Long id);

}
