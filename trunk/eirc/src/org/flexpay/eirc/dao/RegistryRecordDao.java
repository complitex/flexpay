package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.RegistryRecord;
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