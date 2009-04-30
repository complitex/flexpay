package org.flexpay.ab.actions.town;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.actions.nametimedependent.DeleteAction;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.persistence.TownNameTemporal;
import org.flexpay.ab.persistence.TownNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;

public class TownDeleteAction extends DeleteAction<
		TownName, TownNameTemporal, Town, TownNameTranslation> {

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();

	public CountryFilter getCountryFilter() {
		return countryFilter;
	}

	public void setCountryFilter(CountryFilter countryFilter) {
		this.countryFilter = countryFilter;
	}

	public RegionFilter getRegionFilter() {
		return regionFilter;
	}

	public void setRegionFilter(RegionFilter regionFilter) {
		this.regionFilter = regionFilter;
	}

	protected ArrayStack getFilters() {
		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		filters.push(regionFilter);
		return filters;
	}

	protected void setFilters(ArrayStack filters) {
		regionFilter = (RegionFilter) filters.peek(0);
		countryFilter = (CountryFilter) filters.peek(1);
	}

}
