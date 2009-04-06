package org.flexpay.common.dao.registry;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface RegistryRecordDao extends GenericDao<RegistryRecord, Long> {

	/**
	 * List registry records
	 *
	 * @param registryId   Registry header id
	 * @param lowerBoundId Lower bound of records to fetch
	 * @param upperBoundId	Hi bound of records to fetch
	 * @return list of registry records
	 */
	@NotNull
	List<RegistryRecord> listRecordsForProcessing(Long registryId, Long lowerBoundId, Long upperBoundId);
}
