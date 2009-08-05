package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.TownService;
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

		Long regionIdLong = null;

		try {
			if (parents != null) {
				regionIdLong = Long.parseLong(parents[0]);
			} else {
				if (getUserPreferences().getRegionFilterValue() == null) {
					getUserPreferences().setRegionFilterValue(ApplicationConfig.getDefaultRegionStub().getId() + "");
					regionIdLong = ApplicationConfig.getDefaultRegionStub().getId();
				} else {
					regionIdLong = Long.parseLong(getUserPreferences().getRegionFilterValue());
				}
			}
		} catch (Exception e) {
			log.warn("Incorrect region id in filter ({})", parents[0]);
			return SUCCESS;
		}
		if (regionIdLong == null) {
			log.warn("Can't get region id in filter ({})");
			return SUCCESS;
		}

		List<Town> towns = townService.findByRegionAndQuery(new Stub<Region>(regionIdLong), "%" + q + "%");
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
		if (filterValueLong == null) {
			if (getUserPreferences().getRegionFilterValue() != null
					&& !getUserPreferences().getRegionFilterValue().equals(ApplicationConfig.getDefaultRegionStub().getId() + "")) {
				filterValue = "";
			} else {
				filterValue = ApplicationConfig.getDefaultTownStub().getId() + "";
				town = townService.readFull(ApplicationConfig.getDefaultTownStub());
			}
		} else {
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
		getUserPreferences().setTownFilterValue(filterValue);
		getUserPreferences().setDistrictFilterValue("");
		getUserPreferences().setStreetFilterValue("");
		getUserPreferences().setBuildingFilterValue("");
		getUserPreferences().setApartmentFilterValue("");
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

}
