package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.HistoryRecordsFilter;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryRecordsFiltersChain implements HistoryRecordsFilter {

	private List<HistoryRecordsFilter> filters = CollectionUtils.list();

	/**
	 * Check if record is allowed to be chared
	 *
	 * @param record HistoryRecord  to check
	 * @return <code>true</code> if record could be shared, or <code>false</code> otherwise
	 */
    @Override
	public boolean accept(@NotNull HistoryRecord record) {

		for (HistoryRecordsFilter filter : filters) {
			if (!filter.accept(record)) {
				return false;
			}
		}

		return true;
	}

	public void setFilters(List<HistoryRecordsFilter> filters) {
		this.filters = filters;
	}
}
