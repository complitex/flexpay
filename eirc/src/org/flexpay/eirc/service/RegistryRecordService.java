package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.RegistryRecordContainer;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.Collection;

public interface RegistryRecordService {

	/**
	 * Create SpRegistryRecord
	 *
	 * @param spRegistryRecord SpRegistryRecord
	 * @return created SpRegistryRecord object
	 * @throws FlexPayException if failure occurs
	 */
	public RegistryRecord create(RegistryRecord spRegistryRecord)
			throws FlexPayException;

	/**
	 * Read RegistryRecord object by its unique id
	 *
	 * @param id RegistryRecord key
	 * @return RegistryRecord object, or <code>null</code> if object not
	 *         found
	 */
	@Nullable
	RegistryRecord read(Long id);

	/**
	 * Update SpRegistryRecord
	 *
	 * @param spRegistryRecord SpRegistryRecord to update for
	 * @return Updated SpRegistryRecord object
	 * @throws FlexPayException if SpRegistryRecord object is invalid
	 */
	RegistryRecord update(RegistryRecord spRegistryRecord)
			throws FlexPayException;

	void delete(RegistryRecord spRegistryRecord);

	/**
	 * List registry records
	 *
	 * @param registry			  Registry to get records for
	 * @param importErrorTypeFilter Errors type filter
	 * @param recordStatusFilter	Records status filter
	 * @param pager				 Page
	 * @return list of filtered registry records
	 */
	List<RegistryRecord> listRecords(SpRegistry registry, ImportErrorTypeFilter importErrorTypeFilter,
									   RegistryRecordStatusFilter recordStatusFilter, Page<RegistryRecord> pager);

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
	DataSourceDescription getDataSourceDescription(RegistryRecord record);

	/**
	 * Set record status to fixed and invalidate error
	 *
	 * @param record Registry record
	 * @return updated record
	 * @throws Exception if failure occurs
	 */
	RegistryRecord removeError(RegistryRecord record) throws Exception;

	/**
	 * Find registry records by identifiers
	 *
	 * @param registry Registry to get records for
	 * @param objectIds Set of identifiers
	 * @return Records
	 */
	Collection<RegistryRecord> findObjects(SpRegistry registry, Set<Long> objectIds);

	/**
	 * Find containers associated with a registry record
	 *
	 * @param stub Registry record stub
	 * @return List of containers
	 */
	List<RegistryRecordContainer> getRecordContainers(RegistryRecord stub);
}
