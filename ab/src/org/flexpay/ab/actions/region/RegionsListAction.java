package org.flexpay.ab.actions.region;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.sorter.RegionSorter;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
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

		if (countryFilter == null || countryFilter <= 0) {
			log.warn("Incorrect country id in filter ({})", countryFilter);
			return SUCCESS;
		}

		ArrayStack filters = arrayStack(new CountryFilter(countryFilter));
		List<ObjectSorter> sorters = CollectionUtils.<ObjectSorter>list(regionSorter);
		regions = regionService.find(filters, sorters, getPager());

		if (log.isDebugEnabled()) {
			log.debug("Total regions found: {}", regions.size());
		}

		regions = regionService.readFull(DomainObject.collectionIds(regions), true);

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
