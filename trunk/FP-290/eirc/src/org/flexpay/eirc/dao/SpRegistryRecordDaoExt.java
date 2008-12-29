package org.flexpay.eirc.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;

import java.util.Collection;
import java.util.List;

public interface SpRegistryRecordDaoExt {

	/**
	 * List registry records
	 *
	 * @param id	Registry id
	 * @param pager Pager
	 * @return list of records
	 */
	List<RegistryRecord> listRecordsForUpdate(Long id, Page pager);

	/**
	 * Filter registry records
	 *
	 * @param registryId			Registry key
	 * @param importErrorTypeFilter Error type filter
	 * @param recordStatusFilter	Records status filter
	 * @param pager				 Page
	 * @return list of registry records
	 */
	List<RegistryRecord> filterRecords(Long registryId, ImportErrorTypeFilter importErrorTypeFilter,
										 RegistryRecordStatusFilter recordStatusFilter, Page<RegistryRecord> pager);

	/**
	 * Find registries by identifiers
	 *
	 * @param registryId Registr identifier
	 * @param objectIds  Set of registry identifiers
	 * @return collection of registries
	 */
	List<RegistryRecord> findRecords(Long registryId, Collection<Long> objectIds);


	/**
	 * Count number of error in registry
	 *
	 * @param registryId Registry to count errors for
	 * @return number of errors
	 */
	int getErrorsNumber(Long registryId);

	/**
	 * Find data source description for record
	 *
	 * @param id Registry record identifier
	 * @return DataSourceDescription
	 */
	DataSourceDescription getDataSourceDescription(Long id);
}
