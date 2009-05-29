package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.Street;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;

import java.util.Map;

public class StreetNameFilter extends PrimaryKeyFilter<Street> {

	private boolean showSearchString;
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

	public boolean isShowSearchString() {
		return showSearchString;
	}

	public void setShowSearchString(boolean showSearchString) {
		this.showSearchString = showSearchString;
	}

}
