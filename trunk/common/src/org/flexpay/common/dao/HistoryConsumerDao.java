package org.flexpay.common.dao;

import org.flexpay.common.persistence.history.HistoryConsumer;

import java.util.List;

public interface HistoryConsumerDao extends GenericDao<HistoryConsumer, Long> {

	List<HistoryConsumer> listConsumers();
}
