package org.flexpay.ab.actions.apartment;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.actions.buildings.BuildingsActionsBase;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ListApartments extends BuildingsActionsBase implements
		SessionAware {

	private static final String ATTRIBUTE_ACTION_ERRORS = ListApartments.class
			.getName()+ ".ACTION_ERRORS";

	private static Logger log = Logger.getLogger(ListApartments.class);

	private ParentService<BuildingsFilter> parentService;
	private ApartmentService apartmentService;

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	protected StreetFilter streetFilter = new StreetFilter();
	protected BuildingsFilter buildingsFilter = new BuildingsFilter();
	private Page pager = new Page();
	private Map session;

	private List<Apartment> apartments = new ArrayList<Apartment>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings( { "unchecked" })
	public String execute() throws Exception {
		long start = System.currentTimeMillis();
		try {
			ArrayStack filterArrayStack = getFilters();
			for (Object filter : filterArrayStack) {
				((PrimaryKeyFilter) filter).initFilter(session);
			}
			ArrayStack filters = parentService.initFilters(filterArrayStack,
					userPreferences.getLocale());
			setFilters(filters);

			apartments = apartmentService.getApartments(filters, pager);
		} catch (FlexPayException e) {
			addActionError(e);
		}

		// Retrieve action errors from session if any
		if (log.isDebugEnabled()) {
			log.debug("Getting actionErrors: "
					+ session.get(ATTRIBUTE_ACTION_ERRORS));
		}
		Collection errors = (Collection) session
				.remove(ATTRIBUTE_ACTION_ERRORS);
		if (errors != null && !errors.isEmpty()) {
			Collection actionErrors = getActionErrors();
			actionErrors.addAll(errors);
			setActionErrors(actionErrors);
		}

		if (log.isInfoEnabled()) {
			log.info("Listing apartments: "
					+ (System.currentTimeMillis() - start) + " ms");
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
		filters.push(buildingsFilter);

		return filters;
	}

	/**
	 * Setter for property 'filters'.
	 * 
	 * @param filters
	 *            Value to set for property 'filters'.
	 */
	public void setFilters(ArrayStack filters) {
		countryFilter = (CountryFilter) filters.peek(4);
		regionFilter = (RegionFilter) filters.peek(3);
		townFilter = (TownFilter) filters.peek(2);
		streetFilter = (StreetFilter) filters.peek(1);
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
	 * @param countryFilter
	 *            Value to set for property 'countryFilter'.
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
	 * @param regionFilter
	 *            Value to set for property 'regionFilter'.
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
	 * @param townFilter
	 *            Value to set for property 'townFilter'.
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
	 * @param streetFilter
	 *            Value to set for property 'streetFilter'.
	 */
	public void setStreetFilter(StreetFilter streetFilter) {
		this.streetFilter = streetFilter;
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
	 * @param buildingsFilter
	 *            Value to set for property 'buildingsFilter'.
	 */
	public void setBuildingsFilter(BuildingsFilter buildingsFilter) {
		this.buildingsFilter = buildingsFilter;
	}

	/**
	 * Setter for property 'buildingsService'.
	 * 
	 * @param apartmentService
	 *            Value to set for property 'buildingsService'.
	 */
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	/**
	 * Setter for property 'parentService'.
	 * 
	 * @param parentService
	 *            Value to set for property 'parentService'.
	 */
	public void setParentService(ParentService<BuildingsFilter> parentService) {
		this.parentService = parentService;
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
	 * @param pager
	 *            Value to set for property 'pager'.
	 */
	public void setPager(Page pager) {
		this.pager = pager;
	}

	public static void setActionErrors(Map<String, Object> session,
			Collection actionErrors) {
		if (log.isDebugEnabled()) {
			log.debug("Setting actionErrors: " + actionErrors);
		}
		session.put(ATTRIBUTE_ACTION_ERRORS, actionErrors);
	}

	/**
	 * Sets the Map of session attributes in the implementing class.
	 * 
	 * @param session
	 *            a Map of HTTP session attribute name/value pairs.
	 */
	public void setSession(Map session) {
		this.session = session;
	}
}