package org.flexpay.common.dao.registry;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.RecordErrorsGroup;
import org.flexpay.common.persistence.registry.RecordErrorsType;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.persistence.registry.filter.FilterData;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface RegistryRecordDaoExt {

	/**
	 * List registry records for import operation
	 *
	 * @param id	Registry id
	 * @param minId Minimum registry record id to retrieve
	 * @param maxId Maximum registry record id to retrieve
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

    List<RegistryRecord> filterRecords(Long registryId, Collection<ObjectFilter> filters, Page<RegistryRecord> pager);

    List<RegistryRecord> filterRecords(Long registryId, Collection<ObjectFilter> filters,
                                       String criteria, List<Object> params, Page<RegistryRecord> pager);

    List<RegistryRecord> findRecordsWithThisError(RegistryRecord record, String correctionType, Page<RegistryRecord> pager);

	/**
	 * Find registries by identifiers
	 *
	 * @param registryId Registry identifier
	 * @param objectIds  Set of registry identifiers
	 * @return collection of registries
	 */
	List<RegistryRecord> findRecords(Long registryId, Collection<Long> objectIds);

    List<String> findAutocompleterAddresses(Long registryId, FilterData filterData, Page<String> pager);

    List<RecordErrorsType> findErrorsTypes(Long registryId, Collection<ObjectFilter> filters);

    List<RecordErrorsGroup> findErrorsGroups(Long registryId, RecordErrorsGroupSorter sorter, Collection<ObjectFilter> filters, String groupByString, Page<RecordErrorsGroup> pager);

    void updateErrorStatus(Collection<Long> recordIds, RegistryRecordStatus newStatus);

	/**
	 * Count number of error in registry
	 *
	 * @param registryId Registry to count errors for
	 * @return number of errors
	 */
	int getErrorsNumber(Long registryId);

	/**
	 * Get minimum and maximum record ids for processing
	 *
	 * @param registryId Registry identifier to process
	 * @return Minimum-Maximum pair
	 */
	Long[] getMinMaxIdsForProcessing(@NotNull Long registryId);

	/**
	 * Get minimum and maximum record ids for processing
	 *
	 * @param registryId Registry identifier to process
	 * @param constraintMinId Constraint on registry record id
	 * @return Minimum-Maximum pair
	 */
	@NotNull
	Long[] getMinMaxIdsForProcessing(@NotNull Long registryId, @NotNull Long constraintMinId);

	/**
	 * Get minimum and maximum record ids for importing
	 *
	 * @param registryId Registry identifier to import
	 * @return Minimum-Maximum pair
	 */
	@NotNull
	Long[] getMinMaxIdsForImporting(@NotNull Long registryId);

	/**
	 * Check exist registry records with status FIXED and LOADED
	 *
	 * @param registryId Registry Id
	 * @return <code>true</code> if records exist, or <code>false</code> otherwise
	 */
	boolean hasLoadedAndFixedRecords(@NotNull Long registryId);
}
