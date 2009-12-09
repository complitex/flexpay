package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.util.config.AbUserPreferences;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultCountryStub;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultRegionStub;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Search regions by name
 */
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
					up.setCountryFilter(getDefaultCountryStub().getId());
				}
				countryId = up.getCountryFilter();
			}
		} catch (Exception e) {
			log.warn("Incorrect country id in filter ({})", parents[0]);
			addActionError(getText("common.object_not_selected"));
			return SUCCESS;
		}

		if (countryId.equals(0L)) {
			return SUCCESS;
		}

		if (q == null) {
			q = "";
		}

		List<Region> regions = regionService.findByParentAndQuery(new Stub<Country>(countryId), "%" + q + "%");
		if (log.isDebugEnabled()) {
			log.debug("Found regions: {}", regions.size());
		}

		for (Region region : regions) {
			RegionName name = region.getCurrentName();
			if (name == null) {
				log.warn("Found incorrect region: {}", region);
				continue;
			}
			foundObjects.add(new FilterObject(region.getId() + "", getTranslationName(name.getTranslations())));
		}

		return SUCCESS;
	}

	@Override
	public void readFilterString() {

		AbUserPreferences up = getUserPreferences();

		if (filterValueLong == null || filterValueLong <= 0) {
			if (up.getCountryFilter() != null
					&& !up.getCountryFilter().equals(getDefaultCountryStub().getId())) {
				filterValueLong = 0L;
			} else {
				filterValueLong = getDefaultRegionStub().getId();
			}
			filterValue = filterValueLong + "";
		}

		Region region = null;

		if (filterValueLong != 0) {
			region = regionService.readFull(new Stub<Region>(filterValueLong));
			if (region == null) {
				log.warn("Can't get region with id {} from DB", filterValueLong);
				addActionError(getText("common.object_not_selected"));
			}
		}
		if (region != null && region.getCurrentName() != null) {
			filterString = getTranslationName(region.getCurrentName().getTranslations());
		} else {
			filterString = "";
		}
	}

	@Override
	public void saveFilterValue() {

		if (filterValueLong == null || filterValueLong <= 0) {
			log.warn("Incorrect filter value {}", filterValue);
			addActionError(getText("common.error.invalid_id"));
			return;
		}

		Region region = regionService.readFull(new Stub<Region>(filterValueLong));
		if (region == null) {
			log.warn("Can't get region with id {} from DB", filterValueLong);
			addActionError(getText("common.object_not_selected"));
			return;
		} else if (region.isNotActive()) {
			log.warn("Region with id {} is disabled", filterValueLong);
			addActionError(getText("common.object_not_selected"));
			return;
		}

		AbUserPreferences up = getUserPreferences();
		up.setRegionFilter(filterValueLong);
		up.setTownFilter(0L);
		up.setDistrictFilter(0L);
		up.setStreetFilter(0L);
		up.setBuildingFilter(0L);
		up.setApartmentFilter(0L);
	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

}
