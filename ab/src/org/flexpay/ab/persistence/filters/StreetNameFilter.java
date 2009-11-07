package org.flexpay.ab.persistence.filters;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Street;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;

public class StreetNameFilter extends PrimaryKeyFilter<Street> {

	private boolean showSearchString;
	private String searchString;

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public boolean isShowSearchString() {
		return showSearchString;
	}

	public void setShowSearchString(boolean showSearchString) {
		this.showSearchString = showSearchString;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("defaultId", getDefaultId()).
				append("selectedId", getSelectedId()).
				append("searchString", searchString).
				append("allowEmpty", isAllowEmpty()).
				append("needAutoChange", isNeedAutoChange()).
				append("readonly", isReadOnly()).
				append("showSearchString", showSearchString).
				toString();
	}

}
