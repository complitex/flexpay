package org.flexpay.common.service;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorter;
import org.jetbrains.annotations.NotNull;
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
	 * @param stub RegistryRecord stub
	 * @return RegistryRecord object, or <code>null</code> if object not
	 *         found
	 */
	@Nullable
	RegistryRecord read(@NotNull Stub<RegistryRecord> stub);

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
     * List registry records
     *
     * @param registry			  Registry to get records for
     * @param importErrorTypeFilter Errors type filter
     * @param recordStatusFilter	Records status filter
     * @param criteria criteria
     * @param params params
     * @param pager				 Page
     * @return list of filtered registry records
     */
    List<RegistryRecord> listRecords(Registry registry, ImportErrorTypeFilter importErrorTypeFilter, RegistryRecordStatusFilter recordStatusFilter,
                                      String criteria, List<Object> params, Page<RegistryRecord> pager);

    /**
     * List registry records
     *
     * @param record	Record for template
     * @param correctionType Type of correction
     * @param pager				 Page
     * @return list of filtered registry records
     */
    List<RegistryRecord> listRecords(RegistryRecord record, String correctionType, Page<RegistryRecord> pager);

	/**
	 * List registry records
	 *
	 * @param registry			  Registry to get records for
	 * @param range FetchRange
	 * @return list of filtered registry records
	 */
	List<RegistryRecord> listRecordsForExport(Registry registry, FetchRange range);

    List<RecordErrorsGroup> listRecordErrorsGroups(@NotNull Registry registry, RecordErrorsGroupSorter sorter, ImportErrorTypeFilter importErrorTypeFilter, String groupByString, Page<RecordErrorsGroup> pager);

    List<RecordErrorsType> listRecordErrorsTypes(@NotNull Registry registry);

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
     * Set records status to fixed and invalidate error
     *
     * @param records Registry records
     * @throws Exception if failure occurs
     */
    void removeError(Collection<RegistryRecord> records) throws Exception;

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

	/**
	 * Batch create records
	 *
	 * @param records Collection of records to create
	 */
	void create(Collection<RegistryRecord> records);
}
