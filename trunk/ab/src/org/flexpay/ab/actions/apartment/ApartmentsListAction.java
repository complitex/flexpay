package org.flexpay.ab.actions.apartment;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.actions.buildings.BuildingsActionsBase;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ApartmentsListAction extends BuildingsActionsBase {

	private ParentService<BuildingsFilter> parentService;
	protected ApartmentService apartmentService;

	protected CountryFilter countryFilter = new CountryFilter();
	protected RegionFilter regionFilter = new RegionFilter();
	protected TownFilter townFilter = new TownFilter();
	protected StreetNameFilter streetNameFilter = new StreetNameFilter();
	protected BuildingsFilter buildingsFilter = new BuildingsFilter();
	private Page pager = new Page();

	private List<Apartment> apartments = CollectionUtils.list();

	public ApartmentsListAction() {
		streetNameFilter.setShowSearchString(true);
	}

    /**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public String doExecute() throws Exception {

		ArrayStack filters = getFilters();
		for (Object filter : filters) {
			((PrimaryKeyFilter) filter).initFilter(session);
		}

		filters = parentService.initFilters(filters, userPreferences.getLocale());
		setFilters(filters);

		apartments = apartmentService.getApartments(filters, pager);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
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

		return filters;
	}

	/**
	 * Setter for property 'filters'.
	 *
	 * @param filters Value to set for property 'filters'.
	 */
	public void setFilters(ArrayStack filters) {
		countryFilter = (CountryFilter) filters.peek(4);
		regionFilter = (RegionFilter) filters.peek(3);
		townFilter = (TownFilter) filters.peek(2);
		streetNameFilter = (StreetNameFilter) filters.peek(1);
		buildingsFilter = (BuildingsFilter) filters.peek(0);
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
	 * Getter for property 'buildingsFilter'.
	 *
	 * @return Value for property 'buildingsFilter'.
	 */
	public BuildingsFilter getBuildingsFilter() {
		return buildingsFilter;
	}

	/**
	 * Setter for property 'buildingsFilter'.
	 *
	 * @param buildingsFilter Value to set for property 'buildingsFilter'.
	 */
	public void setBuildingsFilter(BuildingsFilter buildingsFilter) {
		this.buildingsFilter = buildingsFilter;
	}

	/**
	 * Getter for property 'buildingsList'.
	 *
	 * @return Value for property 'buildingsList'.
	 */
	public List<Apartment> getApartments() {
		return apartments;
	}

	/**
	 * Getter for property 'pager'.
	 *
	 * @return Value for property 'pager'.
	 */
	public Page getPager() {
		return pager;
	}

	/**
	 * Setter for property 'pager'.
	 *
	 * @param pager Value to set for property 'pager'.
	 */
	public void setPager(Page pager) {
		this.pager = pager;
	}

	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	public void setParentService(ParentService<BuildingsFilter> parentService) {
		this.parentService = parentService;
	}
}
