package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class DistrictFilterAjaxAction extends FilterAjaxAction {

	private DistrictService districtService;

	@NotNull
	public String doExecute() throws FlexPayException {

		if (saveFilterValue()) {
			return SUCCESS;
		}

		Long townIdLong;

		try {
			townIdLong = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect town id in filter ({})", parents[0]);
			return SUCCESS;
		}

		List<District> districts = districtService.findByTownAndQuery(new Stub<Town>(townIdLong), "%" + q + "%");
		log.debug("Found districts: {}", districts);

		foundObjects = new ArrayList<FilterObject>();
		for (District district : districts) {
			FilterObject object = new FilterObject();
			object.setValue(district.getId() + "");
			object.setName(getTranslation(district.getCurrentName().getTranslations()).getName());
			foundObjects.add(object);
		}

		return SUCCESS;
	}

	public boolean saveFilterValue() {
		if (filterValue != null) {
			try {
				UserPreferences prefs = UserPreferences.getPreferences(request);
				prefs.setDistrictFilterValue(Long.parseLong(filterValue));
				UserPreferences.setPreferences(request, prefs);
				return true;
			} catch (Exception e) {
				log.warn("Incorrect country id in filter ({})", filterValue);
			}
		}
		return false;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

}
