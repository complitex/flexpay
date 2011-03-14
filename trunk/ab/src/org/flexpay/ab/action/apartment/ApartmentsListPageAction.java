package org.flexpay.ab.action.apartment;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ApartmentsListPageAction extends FPActionSupport {

	private Long buildingFilter;

	private BuildingService buildingService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (buildingFilter == null || buildingFilter < 0) {
			log.warn("Incorrect filter value {}", buildingFilter);
			addActionError(getText("ab.error.building_address.incorrect_address_id"));
			buildingFilter = 0L;
		} else if (buildingFilter > 0) {
			BuildingAddress address = buildingService.readFullAddress(new Stub<BuildingAddress>(buildingFilter));
			if (address == null) {
				log.warn("Can't get building address with id {} from DB", buildingFilter);
				addActionError(getText("ab.error.building_address.cant_get_address"));
				buildingFilter = 0L;
			} else if (address.isNotActive()) {
				log.warn("Building address with id {} is disabled", buildingFilter);
				addActionError(getText("ab.error.building_address.cant_get_address"));
				buildingFilter = 0L;
			}
		}

		if (hasActionErrors()) {
			return SUCCESS;
		}

		AbUserPreferences up = (AbUserPreferences) getUserPreferences();
		up.setBuildingFilter(buildingFilter);
		up.setApartmentFilter(0L);

		return SUCCESS;

	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setBuildingFilter(Long buildingFilter) {
		this.buildingFilter = buildingFilter;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}
