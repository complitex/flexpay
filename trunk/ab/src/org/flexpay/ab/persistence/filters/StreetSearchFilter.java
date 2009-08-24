package org.flexpay.ab.persistence.filters;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.filter.ObjectFilter;

public class StreetSearchFilter extends ObjectFilter {

	private String searchString;

	public StreetSearchFilter() {
	}

	public StreetSearchFilter(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	@Override
	public boolean needFilter() {
		return StringUtils.isNotBlank(searchString);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("readOnly", isReadOnly()).
				append("searchString", searchString).
				toString();
	}

}
