package org.flexpay.ab.actions.region;

import org.apache.struts2.interceptor.SessionAware;
import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.dao.paging.Page;

import java.util.*;

public class RegionsDelete extends FPActionSupport implements SessionAware {

	private static Logger log = Logger.getLogger(RegionsDelete.class);

	private CountryService countryService;
	private RegionService regionService;

	private Map<String, Object> session;

	private CountryFilter countryFilter = new CountryFilter();

	/**
	 * {@inheritDoc}
	 */
	public String execute() {

		log.info("execute called!");
		try {
			countryFilter = countryService.initFilter(countryFilter, userPreferences.getLocale());

			Collection<Region> regionsToDisable = new ArrayList<Region>();
			for (Region region : regionService.getRegions(countryFilter)) {
				if (regionIds.contains(region.getId())) {
					regionsToDisable.add(region);
				}
			}

			if (!regionsToDisable.isEmpty()) {
				regionService.disable(regionsToDisable);
			} else {
				addActionError(getText("error.no_regions_to_disable"));
			}
		} catch (FlexPayException e) {
			addActionError(e);
		} catch (FlexPayExceptionContainer container) {
			addActionErrors(container);
		}

		// Save action errors for forwarded action
		if (!getActionErrors().isEmpty()) {
			RegionsList.setActionErrors(session, getActionErrors());
		}
		System.out.println("SUCCESS!");
		return SUCCESS;
	}

	private Set<Long> regionIds = new HashSet<Long>();

	/**
	 * Getter for property 'townTypeIds'.
	 *
	 * @return Value for property 'townTypeIds'.
	 */
	public Set<Long> getRegionIds() {
		return regionIds;
	}

	/**
	 * Setter for property 'townTypeIds'.
	 *
	 * @param regionIds Value to set for property 'townTypeIds'.
	 */
	public void setRegionIds(Set<Long> regionIds) {
		this.regionIds = regionIds;
	}

	/**
	 * Sets the Map of session attributes in the implementing class.
	 *
	 * @param session a Map of HTTP session attribute name/value pairs.
	 */
	@SuppressWarnings ("unchecked")
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
	 * Setter for property 'countryService'.
	 *
	 * @param countryService Value to set for property 'countryService'.
	 */
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	/**
	 * Setter for property 'regionService'.
	 *
	 * @param regionService Value to set for property 'regionService'.
	 */
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setPager(Page pager) {
		
	}
}
