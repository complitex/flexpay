package org.flexpay.ab.actions.town;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.actions.nametimedependent.CreateAction;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.persistence.TownNameTemporal;
import org.flexpay.ab.persistence.TownNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownTypeFilter;

public class TownCreate extends CreateAction<
		TownName, TownNameTemporal, Town, TownNameTranslation> {

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownTypeFilter townTypeFilter = new TownTypeFilter();

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
	 * Getter for property 'regionFilter'.
	 *
	 * @return Value for property 'regionFilter'.
	 */
	public RegionFilter getRegionFilter() {
		return regionFilter;
	}

	/**
	 * Setter for property 'regionFilter'.
	 *
	 * @param regionFilter Value to set for property 'regionFilter'.
	 */
	public void setRegionFilter(RegionFilter regionFilter) {
		this.regionFilter = regionFilter;
	}

	/**
	 * Getter for property 'townTypeFilter'.
	 *
	 * @return Value for property 'townTypeFilter'.
	 */
	public TownTypeFilter getTownTypeFilter() {
		return townTypeFilter;
	}

	/**
	 * Setter for property 'townTypeFilter'.
	 *
	 * @param townTypeFilter Value to set for property 'townTypeFilter'.
	 */
	public void setTownTypeFilter(TownTypeFilter townTypeFilter) {
		this.townTypeFilter = townTypeFilter;
	}

	/**
	 * Get initial set of filters for action
	 *
	 * @return Collection of filters
	 */
	protected ArrayStack getFilters() {
		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		filters.push(regionFilter);
		filters.push(townTypeFilter);
		return filters;
	}

	/**
	 * Set filters for action
	 *
	 * @param filters collection of filters
	 */
	protected void setFilters(ArrayStack filters) {
		townTypeFilter = (TownTypeFilter) filters.peek(0);
		regionFilter = (RegionFilter) filters.peek(1);
		countryFilter = (CountryFilter) filters.peek(2);
	}
}
