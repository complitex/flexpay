package org.flexpay.common.service.fetch;

import org.flexpay.common.util.CollectionUtils;

import java.util.Set;

/**
 * Read hint saves set of required to processing properties
 */
public class ReadHints {

	private Set<String> hints = CollectionUtils.set();

	public void setHint(String hint) {
		hints.add(hint);
	}

	public boolean hintSet(String hint) {
		return hints.contains(hint);
	}
}
