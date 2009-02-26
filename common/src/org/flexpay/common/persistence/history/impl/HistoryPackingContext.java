package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.history.HistoryConsumer;
import org.flexpay.common.persistence.history.HistoryConsumptionGroup;
import org.flexpay.common.util.CollectionUtils;

import java.util.Map;

public class HistoryPackingContext {

	private HistoryConsumptionGroup group;
	private HistoryConsumer consumer;
	private FetchRange range;
	private int numberOfDiffs;
	private int numberOfRecords;

	private Map<String, Object> parameters = CollectionUtils.map();

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public HistoryConsumptionGroup getGroup() {
		return group;
	}

	public void setGroup(HistoryConsumptionGroup group) {
		this.group = group;
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
