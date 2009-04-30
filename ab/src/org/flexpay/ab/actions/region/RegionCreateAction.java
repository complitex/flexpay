package org.flexpay.ab.actions.region;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.actions.nametimedependent.CreateAction;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTemporal;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;

public class RegionCreateAction extends CreateAction<
		RegionName, RegionNameTemporal, Region, RegionNameTranslation> {

	private CountryFilter countryFilter = new CountryFilter();

	protected ArrayStack getFilters() {
		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		return filters;
	}

	protected void setFilters(ArrayStack filters) {
		countryFilter = (CountryFilter) filters.peek(0);
	}

	public CountryFilter getCountryFilter() {
		return countryFilter;
	}

	public void setCountryFilter(CountryFilter countryFilter) {
		this.countryFilter = countryFilter;
	}

}
