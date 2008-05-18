package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;

import java.util.List;
import java.util.Set;
import java.util.Collection;

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

	/**
	 * Find data source description for record
	 *
	 * @param record Registry record
	 * @return DataSourceDescription
	 */
	DataSourceDescription getDataSourceDescription(SpRegistryRecord record);

	/**
	 * Set record status to fixed and invalidate error
	 *
	 * @param record Registry record
	 * @return updated record
	 * @throws Exception if failure occurs
	 */
	SpRegistryRecord removeError(SpRegistryRecord record) throws Exception;

	/**
	 * Find registry records by identifiers
	 *
	 * @param registry Registry to get records for
	 * @param objectIds Set of identifiers
	 * @return Records
	 */
	Collection<SpRegistryRecord> findObjects(SpRegistry registry, Set<Long> objectIds);
}
