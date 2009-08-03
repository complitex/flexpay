package org.flexpay.common.service.fetch;

/**
 * Thread local holder of ReadHints
 */
public class ReadHintsHolder {

	private static final ThreadLocal<ReadHints> READ_HINTS = new ThreadLocal<ReadHints>();

	public static void setHints(ReadHints hints) {
		READ_HINTS.set(hints);
	}

	public static ReadHints getHints() {
		return READ_HINTS.get();
	}
}
