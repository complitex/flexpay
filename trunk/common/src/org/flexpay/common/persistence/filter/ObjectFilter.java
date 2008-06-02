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

	/**
	 * Check if filter should be applyed
	 *
	 * @return <code>true</code> if applying filter is valid, or <code>false</code> otherwise
	 */
	public boolean needFilter() {
		return false;
	}
}
