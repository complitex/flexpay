package org.flexpay.common.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.history.HistoryConsumer;
import org.flexpay.common.persistence.history.HistoryConsumptionGroup;

import java.util.List;

public interface HistoryConsumerDao extends GenericDao<HistoryConsumer, Long> {

	List<HistoryConsumer> listConsumers();

	List<HistoryConsumptionGroup> listNotSentGroups(Long consumerId, Page<HistoryConsumptionGroup> pager);
}
