package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

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
					up.setRegionFilter(ApplicationConfig.getDefaultRegionStub().getId());
				}
				regionId = up.getRegionFilter();
			}
		} catch (Exception e) {
			log.warn("Incorrect region id in filter ({})", parents[0]);
			return SUCCESS;
		}
		if (regionId == null) {
			log.warn("Can't get region id in filter ({})");
			return SUCCESS;
		} else if (regionId == 0) {
			return SUCCESS;
		}

		List<Town> towns = townService.findByRegionAndQuery(new Stub<Region>(regionId), "%" + q + "%");
		log.debug("Found towns: {}", towns);

		foundObjects = new ArrayList<FilterObject>();
		for (Town town : towns) {
			FilterObject object = new FilterObject();
			object.setValue(town.getId() + "");
			object.setName(getTranslation(town.getCurrentName().getTranslations()).getName());
			foundObjects.add(object);
		}

		return SUCCESS;
	}

	@Override
	public void readFilterString() {
		Town town = null;
		AbUserPreferences up = getUserPreferences();

		if (filterValueLong == null) {
			if (up.getRegionFilter() != null
					&& !up.getRegionFilter().equals(ApplicationConfig.getDefaultRegionStub().getId())) {
				filterValueLong = 0L;
			} else {
				filterValueLong = ApplicationConfig.getDefaultTownStub().getId();
			}
			filterValue = filterValueLong + "";
		}
		if (filterValueLong > 0) {
			town = townService.readFull(new Stub<Town>(filterValueLong));
		}

		if (town != null && town.getCurrentName() != null) {
			filterString = getTranslation(town.getCurrentName().getTranslations()).getName();
		} else {
			filterString = "";
		}
	}

	@Override
	public void saveFilterValue() {
		getUserPreferences().setTownFilter(filterValueLong);
		getUserPreferences().setDistrictFilter(0L);
		getUserPreferences().setStreetFilter(0L);
		getUserPreferences().setBuildingFilter(0L);
		getUserPreferences().setApartmentFilter(0L);
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

}
