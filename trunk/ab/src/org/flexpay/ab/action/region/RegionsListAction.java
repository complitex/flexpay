package org.flexpay.ab.action.region;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.sorter.RegionSorter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;

public class RegionsListAction extends FPActionWithPagerSupport<Region> {

	private Long countryFilter;
	private List<Region> regions = list();
	private RegionSorter regionSorter = new RegionSorter();

	private RegionService regionService;
	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (!doValidate()) {
			return SUCCESS;
		}

		if (regionSorter == null) {
			log.warn("RegionSorter is null");
			regionSorter = new RegionSorter();
		}

		regionSorter.setLang(getLanguage());

		regions = regionService.find(arrayStack(new CountryFilter(countryFilter)), list(regionSorter), getPager());
		if (log.isDebugEnabled()) {
			log.debug("Total regions found: {}", regions.size());
		}

		regions = regionService.readFull(collectionIds(regions), true);
		if (log.isDebugEnabled()) {
			log.debug("Total full regions found: {}", regions.size());
		}

		return SUCCESS;
	}

	private boolean doValidate() {

		if (countryFilter == null || countryFilter <= 0) {
			log.warn("Incorrect country id in filter ({})", countryFilter);
			addActionError(getText("ab.error.country.incorrect_country_id"));
			countryFilter = 0L;
		} else {
			Country country = countryService.readFull(new Stub<Country>(countryFilter));
			if (country == null) {
				log.warn("Can't get country with id {} from DB", countryFilter);
				addActionError(getText("ab.error.country.cant_get_country"));
				countryFilter = 0L;
			} else if (country.isNotActive()) {
				log.warn("Country with id {} is disabled", countryFilter);
				addActionError(getText("ab.error.country.cant_get_country"));
				countryFilter = 0L;
			}
		}

		return !hasActionErrors();
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

	public void setCountryFilter(Long countryFilter) {
		this.countryFilter = countryFilter;
	}

	public List<Region> getRegions() {
		return regions;
	}

	public RegionSorter getRegionSorter() {
		return regionSorter;
	}

	public void setRegionSorter(RegionSorter regionSorter) {
		this.regionSorter = regionSorter;
	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}
}
