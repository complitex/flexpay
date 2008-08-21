package org.flexpay.ab.actions.street;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.actions.nametimedependent.ListAction;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetName;
import org.flexpay.ab.persistence.StreetNameTemporal;
import org.flexpay.ab.persistence.StreetNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.persistence.filters.StreetNameFilter;
import org.flexpay.common.exception.FlexPayException;

public class StreetsList extends ListAction<
		StreetName, StreetNameTemporal, Street, StreetNameTranslation> {

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetNameFilter streetNameFilter = new StreetNameFilter();

	protected void initObjects(ArrayStack filters) throws FlexPayException {
		objectNames = nameTimeDependentService.find(filters, pager);

		if (log.isDebugEnabled()) {
			log.debug("Search string: " + streetNameFilter.getSearchString());
		}
	}

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
	 * Getter for property 'townFilter'.
	 *
	 * @return Value for property 'townFilter'.
	 */
	public TownFilter getTownFilter() {
		return townFilter;
	}

	/**
	 * Setter for property 'townFilter'.
	 *
	 * @param townFilter Value to set for property 'townFilter'.
	 */
	public void setTownFilter(TownFilter townFilter) {
		this.townFilter = townFilter;
	}

	public StreetNameFilter getStreetNameFilter() {
		return streetNameFilter;
	}

	public void setStreetNameFilter(StreetNameFilter streetNameFilter) {
		this.streetNameFilter = streetNameFilter;
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
		filters.push(townFilter);
		filters.push(streetNameFilter);
		return filters;
	}

	/**
	 * Set filters for action
	 *
	 * @param filters collection of filters
	 */
	protected void setFilters(ArrayStack filters) {
		int n = 0;
		streetNameFilter = (StreetNameFilter) filters.peek(n++);
		townFilter = (TownFilter) filters.peek(n++);
		regionFilter = (RegionFilter) filters.peek(n++);
		countryFilter = (CountryFilter) filters.peek(n);
	}
}