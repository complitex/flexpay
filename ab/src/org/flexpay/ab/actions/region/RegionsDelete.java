package org.flexpay.ab.actions.region;

import org.flexpay.ab.actions.nametimedependent.DeleteAction;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTemporal;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.apache.commons.collections.ArrayStack;

import java.util.ArrayList;
import java.util.Collection;

public class RegionsDelete extends DeleteAction<
		RegionName, RegionNameTemporal, Region, RegionNameTranslation> {

	private CountryFilter countryFilter = new CountryFilter();

	/**
	 * Getter for property 'countryFilter'.
	 *
	 * @return Value for property 'countryFilter'.
	 */
	public CountryFilter getCountryFilter() {
		return countryFilter;
	}

	/**
	 * Setter for property 'countryFilter'.
	 *
	 * @param countryFilter Value to set for property 'countryFilter'.
	 */
	public void setCountryFilter(CountryFilter countryFilter) {
		this.countryFilter = countryFilter;
	}

	/**
	 * Get initial set of filters for action
	 *
	 * @return Collection of filters
	 */
	protected ArrayStack getFilters() {
		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		return filters;
	}

	/**
	 * Set filters for action
	 *
	 * @param filters collection of filters
	 */
	protected void setFilters(ArrayStack filters) {
		countryFilter = (CountryFilter) filters.peek(0);
	}
}
