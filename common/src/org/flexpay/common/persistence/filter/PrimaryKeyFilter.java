package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PrimaryKeyFilter {

	private Long selectedId;

	public PrimaryKeyFilter() {
	}

	public PrimaryKeyFilter(Long selectedId) {
		this.selectedId = selectedId;
	}

	/**
	 * Getter for property 'selectedId'.
	 *
	 * @return Value for property 'selectedId'.
	 */
	public Long getSelectedId() {
		return selectedId;
	}

	/**
	 * Setter for property 'selectedId'.
	 *
	 * @param selectedId Value to set for property 'selectedId'.
	 */
	public void setSelectedId(Long selectedId) {
		this.selectedId = selectedId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Selected id", selectedId)
				.toString();
	}
}
