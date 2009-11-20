package org.flexpay.ab.actions.region;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.sorter.RegionSorter;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class RegionsListAction extends FPActionWithPagerSupport<Region> {

	private Long countryFilter;
	private List<Region> regions = list();
	private RegionSorter regionSorter = new RegionSorter();

	private RegionService regionService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (!doValidate()) {
			return ERROR;
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

		boolean valid = true;

		if (countryFilter == null || countryFilter <= 0) {
			log.warn("Incorrect country id in filter ({})", countryFilter);
			valid = false;
		}
		if (regionSorter == null) {
			log.warn("RegionSorter is null");
			valid = false;
		}

		return valid;
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
		return ERROR;
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

}
