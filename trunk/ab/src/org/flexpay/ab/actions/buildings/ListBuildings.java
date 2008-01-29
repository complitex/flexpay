package org.flexpay.ab.actions.buildings;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.service.ParentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ListBuildings extends BuildingsActionsBase implements SessionAware {

	private static final String ATTRIBUTE_ACTION_ERRORS =
			ListBuildings.class.getName() + ".ACTION_ERRORS";

	private static Logger log = Logger.getLogger(ListBuildings.class);

	private ParentService parentService;
	private BuildingService buildingService;

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetFilter streetFilter = new StreetFilter();
	private Page pager = new Page();
	private Map session;

	private List<Buildings> buildingsList = new ArrayList<Buildings>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings ({"unchecked"})
	public String execute() throws Exception {
		long start = System.currentTimeMillis();
		try {
			ArrayStack filters = parentService.initFilters(getFilters(), userPreferences.getLocale());
			setFilters(filters);

			buildingsList = buildingService.getBuildings(filters, pager);
		} catch (FlexPayException e) {
			addActionError(e);
		}

		// Retrieve action errors from session if any
		if (log.isDebugEnabled()) {
			log.debug("Getting actionErrors: " + session.get(ATTRIBUTE_ACTION_ERRORS));
		}
		Collection errors = (Collection) session.remove(ATTRIBUTE_ACTION_ERRORS);
		if (errors != null && !errors.isEmpty()) {
			Collection actionErrors = getActionErrors();
			actionErrors.addAll(errors);
			setActionErrors(actionErrors);
		}

		if (log.isInfoEnabled()) {
			log.info("Listing " + (System.currentTimeMillis() - start) + " ms");
		}
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

	public static void setActionErrors(Map<String, Object> session, Collection actionErrors) {
		if (log.isDebugEnabled()) {
			log.debug("Setting actionErrors: " + actionErrors);
		}
		session.put(ATTRIBUTE_ACTION_ERRORS, actionErrors);
	}

	/**
	 * Sets the Map of session attributes in the implementing class.
	 *
	 * @param session a Map of HTTP session attribute name/value pairs.
	 */
	public void setSession(Map session) {
		this.session = session;
	}
}
