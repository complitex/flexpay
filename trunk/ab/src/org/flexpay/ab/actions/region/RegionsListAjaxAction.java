package org.flexpay.ab.actions.region;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class RegionsListAjaxAction extends FPActionWithPagerSupport<Region> {

	private String countryId;
	private List<Region> regions = list();

	private RegionService regionService;

	@Override
	@NotNull
	public String doExecute() throws Exception {

		Long countryIdLong = null;
		if (StringUtils.isNotBlank(countryId)) {
			try {
				countryIdLong = Long.parseLong(countryId);
			} catch (Exception e) {
				log.warn("Incorrect country id in filter ({})", countryId);
				return SUCCESS;
			}
		}

		regions = regionService.findSimple(new Stub<Country>(countryIdLong), getPager());
		log.info("Total regions found: {}", regions);

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
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public List<Region> getRegions() {
		return regions;
	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

}
