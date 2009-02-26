package org.flexpay.common.dao;

import org.flexpay.common.persistence.history.HistoryConsumption;

public interface HistoryConsumptionDao extends GenericDao<HistoryConsumption, Long> {

	/**
	 * Delete group consumptions
	 *
	 * @param groupId history consumption group key
	 */
	void deleteGroupConsumptions(Long groupId);
}
