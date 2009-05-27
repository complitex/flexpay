package org.flexpay.common.dao;

import org.flexpay.common.persistence.history.HistoryUnPackData;

import java.util.List;

public interface HistoryUnPackDataDao extends GenericDao<HistoryUnPackData, Long> {

	/**
	 * Find unpack data by source id
	 *
	 * @param sourceInstanceId history source instance id
	 * @return list of unpack data
	 */
	List<HistoryUnPackData> findLastUnpackedData(String sourceInstanceId);
}
