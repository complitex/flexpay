package org.flexpay.common.persistence.filter;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PrimaryKeyFilter extends ObjectFilter {

	private Long selectedId;
	private Long defaultId;
	private boolean disabled;

	public PrimaryKeyFilter() {
	}

	public PrimaryKeyFilter(Long selectedId) {
		this.selectedId = selectedId;
	}

	public void initFilter(Map session) {
		String filterName = this.getClass().getName();
		Long inSessionId = (Long) session.get(filterName);
		if (selectedId == null) {
			if (inSessionId == null) {
				selectedId = defaultId;
			} else {
				selectedId = inSessionId;
			}
		} else {
			session.put(filterName, selectedId);
		}
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
	 * @param selectedId
	 *            Value to set for property 'selectedId'.
	 */
	public void setSelectedId(Long selectedId) {
		this.selectedId = selectedId;
	}

	/**
	 * @return the defaultId
	 */
	public Long getDefaultId() {
		return defaultId;
	}

	/**
	 * @param defaultId
	 *            the defaultId to set
	 */
	public void setDefaultId(Long defaultId) {
		this.defaultId = defaultId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Selected id", selectedId).toString();
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
