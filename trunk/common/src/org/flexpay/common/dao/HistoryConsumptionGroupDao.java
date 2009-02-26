package org.flexpay.common.dao;

import org.flexpay.common.persistence.history.HistoryConsumptionGroup;

import java.util.List;

public interface HistoryConsumptionGroupDao extends GenericDao<HistoryConsumptionGroup, Long> {

	List<HistoryConsumptionGroup> findConsumerGroups(Long consumerId);
}
