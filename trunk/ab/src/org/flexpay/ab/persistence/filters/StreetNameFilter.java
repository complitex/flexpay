package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.Street;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;

import java.util.Map;

public class StreetNameFilter extends PrimaryKeyFilter<Street> {

	private String searchString;

	@Override
	public void initFilter(Map session) {
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
}
