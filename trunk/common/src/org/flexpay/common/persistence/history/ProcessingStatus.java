package org.flexpay.common.persistence.history;

/**
 * History records and diffs processing statuses codes holder
 */
public abstract class ProcessingStatus {

	public static final int STATUS_NEW = 0;
	public static final int STATUS_PROCESSED = 1;
	public static final int STATUS_IGNORED = 2;
}
