package org.flexpay.ab.actions.region;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.TranslationUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RegionsList extends FPActionSupport implements SessionAware {

	private static Logger log = Logger.getLogger(RegionsList.class);

	private static final String ATTRIBUTE_ACTION_ERRORS =
			RegionsList.class.getName() + ".ACTION_ERRORS";

	private Map session;

	private RegionService regionService;
	private CountryService countryService;

	private CountryFilter countryFilter = new CountryFilter();
	private Page pager = new Page();

	private List<RegionName> regionNames;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings ({"unchecked"})
	public String execute() throws Exception {

		long start = System.currentTimeMillis();
		try {
			countryFilter = countryService.initFilter(countryFilter, userPreferences.getLocale());

			regionNames = regionService.getRegionNames(countryFilter, pager);
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

		if (log.isInfoEnabled()) {
			log.info("Listing " + (System.currentTimeMillis() - start) + " ms");
		}
		return SUCCESS;
	}

	public static void setActionErrors(Map<String, Object> session, Collection actionErrors) {
		if (log.isDebugEnabled()) {
			log.debug("Setting actionErrors: " + actionErrors);
		}
		session.put(ATTRIBUTE_ACTION_ERRORS, actionErrors);
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
	 * Sets the Map of session attributes in the implementing class.
	 *
	 * @param session a Map of HTTP session attribute name/value pairs.
	 */
	public void setSession(Map session) {
		this.session = session;
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

	/**
	 * Getter for property 'regionNames'.
	 *
	 * @return Value for property 'regionNames'.
	 */
	public List<RegionName> getRegionNames() {
		return regionNames;
	}

	public RegionNameTranslation getTranslation(Set<RegionNameTranslation> translations)
			throws FlexPayException {
		return (RegionNameTranslation) TranslationUtil.getTranslation(
				translations, userPreferences.getLocale());
	}
}
