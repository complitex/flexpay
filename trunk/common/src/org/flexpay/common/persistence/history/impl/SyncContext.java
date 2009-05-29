package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.history.Diff;
import org.jetbrains.annotations.Nullable;

/**
 * Thread local storage of a currently processing diff
 */
public class SyncContext {

	private static final ThreadLocal<Diff> PROCESSING_DIFF = new ThreadLocal<Diff>();

	public static boolean isSyncing() {
		return PROCESSING_DIFF.get() != null;
	}

	public static Diff getProcessingDiff() {
		return PROCESSING_DIFF.get();
	}

	public static void setProcessingDiff(@Nullable Diff diff) {
		PROCESSING_DIFF.set(diff);
	}
}
