package org.flexpay.ab.actions.town;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class TownsListPageAction extends FPActionSupport {

	private Long regionFilter;

	private RegionService regionService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (regionFilter == null || regionFilter < 0) {
			log.warn("Incorrect filter value {}", regionFilter);
			addActionError(getText("common.error.invalid_id"));
		} else if (regionFilter > 0) {
			Region region = regionService.readFull(new Stub<Region>(regionFilter));
			if (region == null) {
				log.warn("Can't get region with id {} from DB", regionFilter);
				addActionError(getText("common.object_not_selected"));
				regionFilter = null;
			} else if (region.isNotActive()) {
				log.warn("Region with id {} is disabled", regionFilter);
				addActionError(getText("common.object_not_selected"));
				regionFilter = null;
			}
		}

		if (hasActionErrors()) {
			return SUCCESS;
		}

		AbUserPreferences up = (AbUserPreferences) getUserPreferences();
		up.setRegionFilter(regionFilter);
		up.setTownFilter(0L);
		up.setDistrictFilter(0L);
		up.setStreetFilter(0L);
		up.setBuildingFilter(0L);
		up.setApartmentFilter(0L);

		return SUCCESS;

	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setRegionFilter(Long regionFilter) {
		this.regionFilter = regionFilter;
	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}
}
