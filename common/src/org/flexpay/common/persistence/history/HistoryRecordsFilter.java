package org.flexpay.common.persistence.history;

import org.jetbrains.annotations.NotNull;

public interface HistoryRecordsFilter {

	/**
	 * Check if record is allowed to be chared
	 *
	 * @param record HistoryRecord  to check
	 * @return <code>true</code> if record could be shared, or <code>false</code> otherwise
	 */
	boolean accept(@NotNull HistoryRecord record);
}
