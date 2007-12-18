package org.flexpay.ab.actions;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DateInterval;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.dao.paging.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RegionsList extends FPActionSupport
		implements ServletRequestAware, SessionAware {

	private static Logger log = Logger.getLogger(TownTypesList.class);

	private static final String ATTRIBUTE_ACTION_ERRORS =
			RegionsList.class.getName() + ".ACTION_ERRORS";

	private static final String ATTRIBUTE_DATE_INTERVAL =
			RegionsList.class.getName() + ".DATE_INTERVAL";

	private HttpServletRequest request;
	private Map session;

	private RegionService regionService;
	private CountryService countryService;

	private CountryFilter countryFilter = new CountryFilter();
	private DateInterval dateInterval = new DateInterval();
	private Page pager = new Page();

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings ({"unchecked"})
	public String execute() throws Exception {
		try {
			UserPreferences prefs = UserPreferences.getPreferences(request);
			countryFilter = countryService.initFilter(countryFilter, prefs.getLocale());
			setupDateInterval();

			List<RegionName> regionNames = regionService.getRegionNames(
					prefs.getLocale(), countryFilter, pager, dateInterval);
			request.setAttribute("region_names", regionNames);
		} catch (FlexPayException e) {
			addActionError(e);
		}

		// Retrive action errors from session if any
		if (log.isDebugEnabled()) {
			log.debug("Getting actionErrors: " + session.get(ATTRIBUTE_ACTION_ERRORS));
		}
		Collection errors = (Collection) session.remove(ATTRIBUTE_ACTION_ERRORS);
		if (errors != null && !errors.isEmpty()) {
			Collection actionErrors = getActionErrors();
			actionErrors.addAll(errors);
			setActionErrors(actionErrors);
		}

		return SUCCESS;
	}

	public static void setActionErrors(Map<String, Object> session, Collection actionErrors) {
		if (log.isDebugEnabled()) {
			log.debug("Setting actionErrors: " + actionErrors);
		}
		session.put(ATTRIBUTE_ACTION_ERRORS, actionErrors);
	}

	public static void saveDateInterval(HttpServletRequest request)
		throws FlexPayExceptionContainer {

		DateInterval dateInterval = new DateInterval();
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		try {
			dateInterval.setBegin(getDateParam(request, "dateInterval_begin"));
		} catch (FlexPayException e) {
			container.addException(e);
		}
		try {
			dateInterval.setEnd(getDateParam(request, "dateInterval_end"));
		} catch (FlexPayException e) {
			container.addException(e);
		}

		if (!container.getExceptions().isEmpty()) {
			throw container;
		}

		request.getSession().setAttribute(ATTRIBUTE_DATE_INTERVAL, dateInterval);
		if (log.isInfoEnabled()) {
			log.info("Interval saved: " + dateInterval);
		}
	}

	/**
	 * Setter for property 'regionService'.
	 *
	 * @param regionService Value to set for property 'regionService'.
	 */
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	/**
	 * Setter for property 'countryService'.
	 *
	 * @param countryService Value to set for property 'countryService'.
	 */
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	/**
	 * Sets the HTTP request object in implementing classes.
	 *
	 * @param request the HTTP request.
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Sets the Map of session attributes in the implementing class.
	 *
	 * @param session a Map of HTTP session attribute name/value pairs.
	 */
	public void setSession(Map session) {
		this.session = session;
	}

	/**
	 * Getter for property 'dateInterval'.
	 *
	 * @return Value for property 'dateInterval'.
	 */
	public DateInterval getDateInterval() {
		return dateInterval;
	}

	/**
	 * initialiser for property 'dateInterval'.
	 */
	public void setupDateInterval() {
		try {
			dateInterval.setBegin(getDateParam(request, "dateInterval_begin"));
		} catch (FlexPayException e) {
			addActionError(e);
		}
		try {
			dateInterval.setEnd(getDateParam(request, "dateInterval_end"));
		} catch (FlexPayException e) {
			addActionError(e);
		}

		if (session.containsKey(ATTRIBUTE_DATE_INTERVAL)) {
			dateInterval = (DateInterval) session.remove(ATTRIBUTE_DATE_INTERVAL);
		}

		if (log.isInfoEnabled()) {
			log.info("Interval: " + dateInterval);
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
