package org.flexpay.common.dao.registry;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface RegistryRecordDao extends GenericDao<RegistryRecord, Long> {

	/**
	 * List registry records
	 *
	 * @param registryId   Registry header id
	 * @param range FetchRange
	 * @return list of registry records
	 */
	@NotNull
	List<RegistryRecord> listRecordsForProcessing(Long registryId, FetchRange range);

	/**
	 * List registry records
	 *
	 * @param registryId   Registry header id
	 * @param range FetchRange
	 * @return list of registry records
	 */
	@NotNull
	List<RegistryRecord> listLoadedAndFixedRecords(Long registryId, FetchRange range);

	/**
	 * List registry records
	 *
	 * @param recordIds Registry records id
	 * @return list of registry records
	 */
	@NotNull
	List<RegistryRecord> listRecordsForProcessingCollection(Collection<Long> recordIds);

	/**
	 * List registry records
	 *
	 * @param registryId   Registry header id
	 * @param range range
	 * @return list of registry records
	 */
	List<RegistryRecord> listRecordsForExport(Long registryId, FetchRange range);

}
