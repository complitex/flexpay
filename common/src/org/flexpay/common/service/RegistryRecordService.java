package org.flexpay.common.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordContainer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RegistryRecordService {

	/**
	 * Create SpRegistryRecord
	 *
	 * @param spRegistryRecord SpRegistryRecord
	 * @return created SpRegistryRecord object
	 * @throws FlexPayException if failure occurs
	 */
	RegistryRecord create(RegistryRecord spRegistryRecord) throws FlexPayException;

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
	RegistryRecord update(RegistryRecord spRegistryRecord) throws FlexPayException;

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
	List<RegistryRecord> listRecords(Registry registry, ImportErrorTypeFilter importErrorTypeFilter,
									   RegistryRecordStatusFilter recordStatusFilter, Page<RegistryRecord> pager);

	/**
	 * Count number of error in registry
	 *
	 * @param registry Registry to count errors for
	 * @return number of errors
	 */
	int getErrorsNumber(Registry registry);

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
	Collection<RegistryRecord> findObjects(Registry registry, Set<Long> objectIds);

	/**
	 * Find containers associated with a registry record
	 *
	 * @param stub Registry record stub
	 * @return List of containers
	 */
	List<RegistryRecordContainer> getRecordContainers(RegistryRecord stub);
}
