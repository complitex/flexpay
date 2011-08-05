package org.flexpay.common.persistence.history;

import org.flexpay.common.service.JpaSetService;
import org.jetbrains.annotations.NotNull;

/**
 * Handles external and internal diffs
 */
public interface HistoryHandler extends JpaSetService {

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	boolean supports(@NotNull Diff diff);

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 * @throws Exception if failure occurs
	 */
	void process(@NotNull Diff diff) throws Exception;
}
