package org.flexpay.ab.actions.buildings;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;

import java.util.ArrayList;
import java.util.List;

public class ListBuildings extends BuildingsActionsBase {

	private ParentService parentService;
	private BuildingService buildingService;

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetFilter streetFilter = new StreetFilter();
	private Page pager = new Page();

	private List<Buildings> buildingsList = new ArrayList<Buildings>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doExecute() throws Exception {

		ArrayStack filterArrayStack = getFilters();
		for (Object filter : filterArrayStack) {
			((PrimaryKeyFilter) filter).initFilter(session);
		}

		ArrayStack filters = parentService.initFilters(filterArrayStack, userPreferences.getLocale());
		setFilters(filters);

		buildingsList = buildingService.getBuildings(filters, pager);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
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
		filters.push(streetFilter);

		return filters;
	}

	/**
	 * Setter for property 'filters'.
	 *
	 * @param filters Value to set for property 'filters'.
	 */
	public void setFilters(ArrayStack filters) {
		countryFilter = (CountryFilter) filters.peek(3);
		regionFilter = (RegionFilter) filters.peek(2);
		townFilter = (TownFilter) filters.peek(1);
		streetFilter = (StreetFilter) filters.peek(0);
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

	/**
	 * Getter for property 'streetFilter'.
	 *
	 * @return Value for property 'streetFilter'.
	 */
	public StreetFilter getStreetFilter() {
		return streetFilter;
	}

	/**
	 * Setter for property 'streetFilter'.
	 *
	 * @param streetFilter Value to set for property 'streetFilter'.
	 */
	public void setStreetFilter(StreetFilter streetFilter) {
		this.streetFilter = streetFilter;
	}

	/**
	 * Setter for property 'buildingsService'.
	 *
	 * @param buildingService Value to set for property 'buildingsService'.
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	/**
	 * Setter for property 'parentService'.
	 *
	 * @param parentService Value to set for property 'parentService'.
	 */
	public void setParentService(ParentService parentService) {
		this.parentService = parentService;
	}

	/**
	 * Getter for property 'buildingsList'.
	 *
	 * @return Value for property 'buildingsList'.
	 */
	public List<Buildings> getBuildingsList() {
		return buildingsList;
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
}
