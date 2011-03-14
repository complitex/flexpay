package org.flexpay.ab.action.street;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class StreetsListPageAction extends FPActionSupport {

	private Long townFilter;

	private TownService townService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (townFilter == null || townFilter < 0) {
			log.warn("Incorrect filter value {}", townFilter);
			addActionError(getText("ab.error.town.incorrect_town_id"));
			townFilter = 0L;
		} else if (townFilter > 0) {
			Town town = townService.readFull(new Stub<Town>(townFilter));
			if (town == null) {
				log.warn("Can't get town with id {} from DB", townFilter);
				addActionError(getText("ab.error.town.cant_get_town"));
				townFilter = 0L;
			} else if (town.isNotActive()) {
				log.warn("Town with id {} is disabled", townFilter);
				addActionError(getText("ab.error.town.cant_get_town"));
				townFilter = 0L;
			}
		}

		if (hasActionErrors()) {
			return SUCCESS;
		}

		AbUserPreferences up = (AbUserPreferences) getUserPreferences();
		up.setTownFilter(townFilter);
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

	public void setTownFilter(Long townFilter) {
		this.townFilter = townFilter;
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}
}
