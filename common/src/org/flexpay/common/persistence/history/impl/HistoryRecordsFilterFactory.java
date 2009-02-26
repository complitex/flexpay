package org.flexpay.common.persistence.history.impl;

public class HistoryRecordsFilterFactory {

	private HistoryRecordsFiltersChain filter;

	public HistoryRecordsFiltersChain getFilter() {
		if (filter == null) {
			filter = new HistoryRecordsFiltersChain();
		}
		return filter;
	}
}
