package org.flexpay.common.persistence.filter;

/**
 * Base class for all filters
 */
public class ObjectFilter {

	private boolean readOnly;

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
}
