package org.flexpay.sz.service;

import java.util.List;

import org.flexpay.common.dao.paging.Page;

public interface RecordService<R> {
	/**
	 * Create Record
	 * 
	 * @param record
	 *            Record
	 * @return created Record object
	 */
	R create(R record);
	
	List<R> findObjects(Page<R> pager, Long szFileId);

}
