package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Map;
import java.io.Serializable;

/**
 * Base class for all filters
 */
public class ObjectFilter implements Serializable {

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

	public void initFilter(Map session) {
		
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("readOnly", readOnly)
				.toString();
	}
}
