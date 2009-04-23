package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class RegionFilterAjaxAction extends FilterAjaxAction {

	private RegionService regionService;

	@NotNull
	public String doExecute() throws FlexPayException {

		Long countryIdLong;

		try {
			if (parents != null) {
				countryIdLong = Long.parseLong(parents[0]);
			} else {
				if (userPreferences.getCountryFilterValue() == null) {
					userPreferences.setCountryFilterValue(ApplicationConfig.getDefaultCountryStub().getId() + "");
					countryIdLong = ApplicationConfig.getDefaultCountryStub().getId();
				} else {
					countryIdLong = Long.parseLong(userPreferences.getCountryFilterValue());
				}
			}
		} catch (Exception e) {
			log.warn("Incorrect country id in filter ({})", parents[0]);
			return SUCCESS;
		}
		if (countryIdLong == null) {
			log.warn("Can't get country id in filter ({})");
			return SUCCESS;
		}

		List<Region> regions = regionService.findByCountryAndQuery(new Stub<Country>(countryIdLong), "%" + q + "%");
		log.debug("Found regions: {}", regions);

		foundObjects = new ArrayList<FilterObject>();
		for (Region region : regions) {
			FilterObject object = new FilterObject();
			object.setValue(region.getId() + "");
			object.setName(getTranslation(region.getCurrentName().getTranslations()).getName());
			foundObjects.add(object);
		}

		return SUCCESS;
	}

	public void readFilterString() {
		Region region = null;
		if (filterValueLong == null) {
			if (userPreferences.getCountryFilterValue() != null
					&& !userPreferences.getCountryFilterValue().equals(ApplicationConfig.getDefaultCountryStub().getId() + "")) {
				filterValue = "";
			} else {
				filterValue = ApplicationConfig.getDefaultRegionStub().getId() + "";
				region = regionService.readFull(ApplicationConfig.getDefaultRegionStub());
			}
		} else {
			region = regionService.readFull(new Stub<Region>(filterValueLong));
		}
		if (region != null && region.getCurrentName() != null) {
			filterString = getTranslation(region.getCurrentName().getTranslations()).getName();
		} else {
			filterString = "";
		}
	}

	public void saveFilterValue() {
		userPreferences.setRegionFilterValue(filterValue);
		userPreferences.setTownFilterValue("");
		userPreferences.setDistrictFilterValue("");
		userPreferences.setStreetFilterValue("");
		userPreferences.setBuildingFilterValue("");
		userPreferences.setApartmentFilterValue("");
	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

}
