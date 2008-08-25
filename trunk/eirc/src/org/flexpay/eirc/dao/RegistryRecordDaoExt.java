package org.flexpay.eirc.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface RegistryRecordDaoExt {

	/**
	 * List registry records for import operation
	 *
	 * @param id	Registry id
	 * @param minId Minimum registry record id to retrive
	 * @param maxId Maximum registry record id to retrive
	 * @return list of records
	 */
	List<RegistryRecord> listRecordsForImport(Long id, Long minId, Long maxId);

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

	/**
	 * Get minimum and maximum record ids for processing
	 *
	 * @param registryId Registry identifier to process
	 * @return Minimum-Maximum pair
	 */
	Long[] getMinMaxIdsForProcessing(@NotNull Long registryId);

	/**
	 * Get minimum and maximum record ids for importing
	 *
	 * @param registryId Registry identifier to import
	 * @return Minimum-Maximum pair
	 */
	@NotNull
	Long[] getMinMaxIdsForImporting(@NotNull Long registryId);
}
