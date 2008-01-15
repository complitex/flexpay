package org.flexpay.ab.actions.town;

import org.flexpay.ab.actions.nametimedependent.DeleteAction;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.persistence.TownNameTemporal;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

public class TownsDelete extends DeleteAction<
		TownName, TownNameTemporal, Town, TownNameTranslation> {

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();

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
	 * Get initial set of filters for action
	 *
	 * @return Collection of filters
	 */
	protected Collection<PrimaryKeyFilter> getFilters() {
		Collection<PrimaryKeyFilter> filters = new ArrayList<PrimaryKeyFilter>();
		filters.add(countryFilter);
		filters.add(regionFilter);
		return filters;
	}

	/**
	 * Set filters for action
	 *
	 * @param filters collection of filters
	 */
	protected void setFilters(Collection<PrimaryKeyFilter> filters) {
		Iterator it = filters.iterator();
		countryFilter = (CountryFilter) it.next();
		regionFilter = (RegionFilter) it.next();
	}
}
