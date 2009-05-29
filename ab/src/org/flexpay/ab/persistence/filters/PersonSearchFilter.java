package org.flexpay.ab.persistence.filters;

import org.flexpay.common.persistence.filter.ObjectFilter;
import org.apache.commons.lang.StringUtils;

/**
 * Helper filter
 */
public class PersonSearchFilter extends ObjectFilter {

	private String searchString;

	public PersonSearchFilter() {
	}

	public PersonSearchFilter(String searchString) {
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

}
