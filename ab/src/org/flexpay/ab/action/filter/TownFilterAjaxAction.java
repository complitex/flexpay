package org.flexpay.ab.action.filter;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.util.config.AbUserPreferences;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultRegionStub;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultTownStub;

import org.flexpay.common.action.filter.FilterAjaxAction;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.set;

import org.flexpay.common.persistence.filter.FilterObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Set;

/**
 * Search towns by name
 */
public class TownFilterAjaxAction extends FilterAjaxAction {

	private TownService townService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		Long regionId;
		AbUserPreferences up = getUserPreferences();

		try {
			if (parents != null) {
				regionId = Long.parseLong(parents[0]);
			} else {
				if (up.getRegionFilter() == null || up.getRegionFilter() == 0) {
					up.setRegionFilter(getDefaultRegionStub().getId());
				}
				regionId = up.getRegionFilter();
			}
		} catch (Exception e) {
			log.warn("Incorrect region id in filter ({})", parents[0]);
			addActionError(getText("ab.error.region.incorrect_region_id"));
			return SUCCESS;
		}

		if (regionId.equals(0L)) {
			return SUCCESS;
		}

		if (q == null) {
			q = "";
		}

		List<Town> towns = townService.findByParentAndQuery(new Stub<Region>(regionId), "%" + q + "%");
		if (log.isDebugEnabled()) {
			log.debug("Found towns: {}", towns.size());
		}

		Set<Long> townIds = set();

		for (Town town : towns) {
			townIds.add(town.getId());
		}

		towns = townService.readFull(townIds, true);

		for (Town town : towns) {
			TownName name = town.getCurrentName();
			if (name == null) {
				log.warn("Found incorrect town: {}", town);
				continue;
			}
			foundObjects.add(new FilterObject(town.getId() + "", getTranslationName(name.getTranslations())));
		}

		return SUCCESS;
	}

	@Override
	public void readFilterString() {
		Town town = null;
		AbUserPreferences up = getUserPreferences();

		if (filterValueLong == null || filterValueLong <= 0) {
			if (up.getRegionFilter() != null
					&& !up.getRegionFilter().equals(getDefaultRegionStub().getId())) {
				filterValueLong = 0L;
			} else {
				filterValueLong = getDefaultTownStub().getId();
			}
			filterValue = filterValueLong + "";
		}
		if (filterValueLong > 0) {
			town = townService.readFull(new Stub<Town>(filterValueLong));
			if (town == null) {
				log.warn("Can't get town with id {} from DB", filterValueLong);
				addActionError(getText("ab.error.town.cant_get_town"));
			}
		}

		if (town != null && town.getCurrentName() != null) {
			filterString = getTranslationName(town.getCurrentName().getTranslations());
		} else {
			filterString = "";
		}
	}

	@Override
	public void saveFilterValue() {

		if (filterValueLong == null || filterValueLong <= 0) {
			log.warn("Incorrect filter value {}", filterValue);
			addActionError(getText("ab.error.town.incorrect_town_id"));
			return;
		}

		Town town = townService.readFull(new Stub<Town>(filterValueLong));
		if (town == null) {
			log.warn("Can't get town with id {} from DB", filterValueLong);
			addActionError(getText("ab.error.town.cant_get_town"));
			return;
		} else if (town.isNotActive()) {
			log.warn("Town with id {} is disabled", filterValueLong);
			addActionError(getText("ab.error.town.cant_get_town"));
			return;
		}

		AbUserPreferences up = getUserPreferences();
		up.setTownFilter(filterValueLong);
		up.setDistrictFilter(0L);
		up.setStreetFilter(0L);
		up.setBuildingFilter(0L);
		up.setApartmentFilter(0L);

	}

    @Override
    public AbUserPreferences getUserPreferences() {
        return (AbUserPreferences) super.getUserPreferences();
    }

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

}
