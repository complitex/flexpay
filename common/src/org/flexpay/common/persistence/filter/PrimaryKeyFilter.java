package org.flexpay.common.persistence.filter;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.DomainObject;
import org.jetbrains.annotations.NotNull;

public class PrimaryKeyFilter<T extends DomainObject> extends ObjectFilter {

	private Long selectedId;
	private Long defaultId;

	public PrimaryKeyFilter() {
	}

	public PrimaryKeyFilter(Long selectedId) {
		this.selectedId = selectedId;
	}

	@Override
	public boolean needFilter() {
		return selectedId != null && selectedId > 0;
	}

	@SuppressWarnings({"unchecked"})
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

	@NotNull
	public Stub<T> getSelectedStub() {
		return new Stub<T>(selectedId);
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
}
