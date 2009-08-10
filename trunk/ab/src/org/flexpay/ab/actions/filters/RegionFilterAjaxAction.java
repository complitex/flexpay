package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class RegionFilterAjaxAction extends FilterAjaxAction {

	private RegionService regionService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		Long countryId;
		AbUserPreferences up = getUserPreferences();

		try {
			if (parents != null) {
				countryId = Long.parseLong(parents[0]);
			} else {
				if (up.getCountryFilter() == null || up.getCountryFilter() == 0) {
					up.setCountryFilter(ApplicationConfig.getDefaultCountryStub().getId());
				}
				countryId = up.getCountryFilter();
			}
		} catch (Exception e) {
			log.warn("Incorrect country id in filter ({})", parents[0]);
			return SUCCESS;
		}
		if (countryId == null) {
			log.warn("Can't get country id in filter ({})");
			return SUCCESS;
		} else if (countryId == 0) {
			return SUCCESS;
		}

		List<Region> regions = regionService.findByCountryAndQuery(new Stub<Country>(countryId), "%" + q + "%");
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

	@Override
	public void readFilterString() {
		Region region = null;
		AbUserPreferences up = getUserPreferences();

		if (filterValueLong == null) {
			if (up.getCountryFilter() != null
					&& !up.getCountryFilter().equals(ApplicationConfig.getDefaultCountryStub().getId() + "")) {
				filterValueLong = 0L;
			} else {
				filterValueLong = ApplicationConfig.getDefaultRegionStub().getId();
			}
			filterValue = filterValueLong + "";
		}
		if (filterValueLong > 0) {
			region = regionService.readFull(new Stub<Region>(filterValueLong));
		}
		if (region != null && region.getCurrentName() != null) {
			filterString = getTranslation(region.getCurrentName().getTranslations()).getName();
		} else {
			filterString = "";
		}
	}

	@Override
	public void saveFilterValue() {
		getUserPreferences().setRegionFilter(filterValueLong);
		getUserPreferences().setTownFilter(0L);
		getUserPreferences().setDistrictFilter(0L);
		getUserPreferences().setStreetFilter(0L);
		getUserPreferences().setBuildingFilter(0L);
		getUserPreferences().setApartmentFilter(0L);
	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

}
