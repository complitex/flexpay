package org.flexpay.common.persistence.history;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * ObjectSyncer implementation can handle objects diffs
 */
public interface ObjectsSyncer {

	/**
	 * ProcessInstance history
	 *
	 * @param diffs History records to process
	 */
	boolean processHistory(@NotNull List<Diff> diffs);
}
