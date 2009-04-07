package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class RegionFilterAjaxAction extends FilterAjaxAction {

	private RegionService regionService;

	@NotNull
	public String doExecute() throws FlexPayException {

		if (saveFilterValue()) {
			return SUCCESS;
		}

		Long countryIdLong;

		try {
			countryIdLong = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect country id in filter ({})", parents[0]);
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

	public boolean saveFilterValue() {
		if (filterValue != null) {
			try {
				UserPreferences prefs = UserPreferences.getPreferences(request);
				prefs.setRegionFilterValue(Long.parseLong(filterValue));
				UserPreferences.setPreferences(request, prefs);
				return true;
			} catch (Exception e) {
				log.warn("Incorrect country id in filter ({})", filterValue);
			}
		}
		return false;
	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

}
