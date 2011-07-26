package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.HistoryRecordsFilter;
import org.jetbrains.annotations.NotNull;

/**
 * Filter that accepts all records
 */
public class NopHistoryRecordsFilter implements HistoryRecordsFilter {

	/**
	 * Check if record is allowed to be chared
	 *
	 * @param record HistoryRecord  to check
	 * @return <code>true</code> if record could be shared, or <code>false</code> otherwise
	 */
    @Override
	public boolean accept(@NotNull HistoryRecord record) {
		return true;
	}
}
