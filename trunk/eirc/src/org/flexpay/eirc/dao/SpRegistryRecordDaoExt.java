package org.flexpay.eirc.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;

import java.util.List;

public interface SpRegistryRecordDaoExt {

	/**
	 * List registry records
	 *
	 * @param id	Registry id
	 * @param pager Pager
	 * @return list of records
	 */
	List<SpRegistryRecord> listRecordsForUpdate(Long id, Page pager);

	/**
	 * Filter registry records
	 *
	 * @param registryId Registry key
	 * @param importErrorTypeFilter Error type filter
	 * @param recordStatusFilter Records status filter
	 * @param pager Page
	 * @return list of registry records
	 */
	List<SpRegistryRecord> filterRecords(Long registryId, ImportErrorTypeFilter importErrorTypeFilter,
										 RegistryRecordStatusFilter recordStatusFilter, Page<SpRegistryRecord> pager);

	/**
	 * Count number of error in registry
	 *
	 * @param registryId Registry to count errors for
	 * @return number of errors
	 */
	int getErrorsNumber(Long registryId);
}