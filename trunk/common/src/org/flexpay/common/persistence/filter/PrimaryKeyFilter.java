package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PrimaryKeyFilter<T extends DomainObject> extends ObjectFilter {

	private Long selectedId;
	private Long defaultId;
	private boolean needAutoChange = true;
	private boolean allowEmpty = true;

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
	@Override
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
	@Nullable
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


	public void unsetSelected() {
		this.selectedId = defaultId;
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
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("readOnly", isReadOnly())
				.append("selectedId", selectedId)
				.append("needAutoChange", needAutoChange)
				.append("allowEmpty", allowEmpty)
				.toString();
	}

	public void setNeedAutoChange(boolean needAutoChange) {
		this.needAutoChange = needAutoChange;
	}

	/**
	 * Check if automatic dependent elements changes is allowed for filter
	 *
	 * @return <code>true</code> if auto change allowed, or <code>false</code> otherwise
	 */
	public boolean isNeedAutoChange() {
		return needAutoChange;
	}

	public boolean isAllowEmpty() {
		return allowEmpty;
	}

	public void setAllowEmpty(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}
}
