package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.SpRegistryRecord;

import java.util.List;

public interface SpRegistryRecordDao extends GenericDao<SpRegistryRecord, Long> {

	/**
	 * List registry records
	 *
	 * @param id Registry id
	 * @param pager Pager
	 * @return list of records
	 */
	List<SpRegistryRecord> listRecords(Long id, Page pager);
}