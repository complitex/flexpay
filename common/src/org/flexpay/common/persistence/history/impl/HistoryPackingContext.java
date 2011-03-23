package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.history.HistoryConsumer;
import org.flexpay.common.persistence.history.HistoryConsumptionGroup;
import org.flexpay.common.util.CollectionUtils;

import java.util.List;
import java.util.Map;

public class HistoryPackingContext {

	private List<HistoryConsumptionGroup> groups = CollectionUtils.list();
	private HistoryConsumer consumer;
	private FetchRange range;
	private int lastNumberOfDiffs;
	private int numberOfDiffs;
	private int numberOfRecords;

	private Map<String, Object> parameters = CollectionUtils.map();

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public List<HistoryConsumptionGroup> getGroups() {
		return groups;
	}

	public void addGroup(HistoryConsumptionGroup group) {
		groups.add(group);
	}

	public HistoryConsumptionGroup getLastGroup() {
		return groups.get(groups.size() - 1);
	}

	public HistoryConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(HistoryConsumer consumer) {
		this.consumer = consumer;
	}

	public int getNumberOfDiffs() {
		return numberOfDiffs;
	}

	public void addDiff() {
		++numberOfDiffs;
		++lastNumberOfDiffs;
	}

	public int getLastNumberOfDiffs() {
		return lastNumberOfDiffs;
	}

	public void resetLastNumberOfDiffs() {
		lastNumberOfDiffs = 0;
	}

	public int getNumberOfRecords() {
		return numberOfRecords;
	}

	public void addRecord() {
		++numberOfRecords;
	}

	public FetchRange getRange() {
		return range;
	}

	public void setRange(FetchRange range) {
		this.range = range;
	}
}
