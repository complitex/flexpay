package org.flexpay.payments.actions.search;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.ab.service.ApartmentService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.collections.ArrayStack;

public class SearchByAddressAction extends FPActionSupport {

	// filters
	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetNameFilter streetNameFilter = new StreetNameFilter();
	private BuildingsFilter buildingsFilter = new BuildingsFilter();
	private ApartmentFilter apartmentFilter = new ApartmentFilter();

	// services
	private ParentService parentService;

	@NotNull
	protected String doExecute() throws Exception {

		initFilters();
		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {

		return SUCCESS;
	}

	 // filters functions 
	private void initFilters() {
		try {
			ArrayStack filterArrayStack = getFilters();
			for (Object filter : filterArrayStack) {
				((ObjectFilter) filter).initFilter(session);
			}
			ArrayStack filters = parentService.initFilters(filterArrayStack, userPreferences.getLocale());
			setFilters(filters);
		} catch (FlexPayException e) {
			addActionError(e);
			log.info("Failed init filters", e);
		}
	}

	private int setFilters(ArrayStack filters) {
		int n = filters.size();
		countryFilter = (CountryFilter) filters.peek(--n);
		regionFilter = (RegionFilter) filters.peek(--n);
		townFilter = (TownFilter) filters.peek(--n);
		streetNameFilter = (StreetNameFilter) filters.peek(--n);
		buildingsFilter = (BuildingsFilter) filters.peek(--n);
		apartmentFilter = (ApartmentFilter) filters.peek(--n);
		return n;
	}

	/**
	 * Getter for property 'filters'.
	 *
	 * @return Value for property 'filters'.
	 */
	public ArrayStack getFilters() {

		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		filters.push(regionFilter);
		filters.push(townFilter);
		filters.push(streetNameFilter);
		filters.push(buildingsFilter);
		filters.push(apartmentFilter);

		return filters;
	}

	// filter getters/setters
	public CountryFilter getCountryFilter() {
		return countryFilter;
	}

	public RegionFilter getRegionFilter() {
		return regionFilter;
	}

	public TownFilter getTownFilter() {
		return townFilter;
	}

	public StreetNameFilter getStreetNameFilter() {
		return streetNameFilter;
	}

	public BuildingsFilter getBuildingsFilter() {
		return buildingsFilter;
	}

	public ApartmentFilter getApartmentFilter() {
		return apartmentFilter;
	}

	public void setCountryFilter(CountryFilter countryFilter) {
		this.countryFilter = countryFilter;
	}

	public void setRegionFilter(RegionFilter regionFilter) {
		this.regionFilter = regionFilter;
	}

	public void setTownFilter(TownFilter townFilter) {
		this.townFilter = townFilter;
	}

	public void setStreetNameFilter(StreetNameFilter streetNameFilter) {
		this.streetNameFilter = streetNameFilter;
	}

	public void setBuildingsFilter(BuildingsFilter buildingsFilter) {
		this.buildingsFilter = buildingsFilter;
	}

	public void setApartmentFilter(ApartmentFilter apartmentFilter) {
		this.apartmentFilter = apartmentFilter;
	}


	// required services
	@Required
	public void setParentService(ParentService parentService) {
		this.parentService = parentService;
	}
}
