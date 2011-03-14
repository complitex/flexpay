package org.flexpay.ab.action.building;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class BuildingsListPageAction extends FPActionSupport {

	private Long streetFilter;

	private StreetService streetService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (streetFilter == null || streetFilter < 0) {
			log.warn("Incorrect filter value {}", streetFilter);
			addActionError(getText("ab.error.street.incorrect_street_id"));
			streetFilter = 0L;
		} else if (streetFilter > 0) {
			Street street = streetService.readFull(new Stub<Street>(streetFilter));
			if (street == null) {
				log.warn("Can't get street with id {} from DB", streetFilter);
				addActionError(getText("ab.error.street.cant_get_street"));
				streetFilter = 0L;
			} else if (street.isNotActive()) {
				log.warn("Street with id {} is disabled", streetFilter);
				addActionError(getText("ab.error.street.cant_get_street"));
				streetFilter = 0L;
			}
		}

		if (hasActionErrors()) {
			return SUCCESS;
		}

		AbUserPreferences up = (AbUserPreferences) getUserPreferences();
		up.setStreetFilter(streetFilter);
		up.setBuildingFilter(0L);
		up.setApartmentFilter(0L);

		return SUCCESS;

	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setStreetFilter(Long streetFilter) {
		this.streetFilter = streetFilter;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}
}
