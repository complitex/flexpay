package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;

import java.util.List;

public interface SpRegistryRecordService {

	/**
	 * Create SpRegistryRecord
	 *
	 * @param spRegistryRecord SpRegistryRecord
	 * @return created SpRegistryRecord object
	 * @throws FlexPayException if failure occurs
	 */
	public SpRegistryRecord create(SpRegistryRecord spRegistryRecord)
			throws FlexPayException;

	/**
	 * Read SpRegistryRecord object by its unique id
	 *
	 * @param id SpRegistryRecord key
	 * @return SpRegistryRecord object, or <code>null</code> if object not
	 *         found
	 */
	SpRegistryRecord read(Long id);

	/**
	 * Update SpRegistryRecord
	 *
	 * @param spRegistryRecord SpRegistryRecord to update for
	 * @return Updated SpRegistryRecord object
	 * @throws FlexPayException if SpRegistryRecord object is invalid
	 */
	SpRegistryRecord update(SpRegistryRecord spRegistryRecord)
			throws FlexPayException;

	void delete(SpRegistryRecord spRegistryRecord);

	/**
	 * List registry records
	 *
	 * @param registry			  Registry to get records for
	 * @param importErrorTypeFilter Errors type filter
	 * @param recordStatusFilter	Records status filter
	 * @param pager				 Page
	 * @return list of filtered registry records
	 */
	List<SpRegistryRecord> listRecords(SpRegistry registry, ImportErrorTypeFilter importErrorTypeFilter,
									   RegistryRecordStatusFilter recordStatusFilter, Page<SpRegistryRecord> pager);

	/**
	 * Count number of error in registry
	 *
	 * @param registry Registry to count errors for
	 * @return number of errors
	 */
	int getErrorsNumber(SpRegistry registry);
}
