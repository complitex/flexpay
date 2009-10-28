package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DistrictFilterAjaxAction extends FilterAjaxAction {

	private DistrictService districtService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		Long townId;

		try {
			townId = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect town id in filter ({})", parents[0]);
			return SUCCESS;
		}
		if (townId == 0) {
			return SUCCESS;
		}

		List<District> districts = districtService.findByTownAndQuery(new Stub<Town>(townId), "%" + q + "%");
		if (log.isDebugEnabled()) {
			log.debug("Found districts: {}", districts.size());
		}

		for (District district : districts) {
			DistrictName name = district.getCurrentName();
			if (name == null) {
				log.warn("Found incorrect district: {}", district);
				continue;
			}
			foundObjects.add(new FilterObject(district.getId() + "", getTranslationName(name.getTranslations())));
		}

		return SUCCESS;
	}

	@Override
	public void readFilterString() {
		if (filterValueLong != null && filterValueLong > 0) {
			District district = districtService.readFull(new Stub<District>(filterValueLong));
			if (district != null && district.getCurrentName() != null) {
				filterString = getTranslationName(district.getCurrentName().getTranslations());
			} else {
				filterString = "";
			}
		} else {
			filterString = "";
		}
	}

	@Override
	public void saveFilterValue() {
		getUserPreferences().setDistrictFilter(filterValueLong);
		getUserPreferences().setBuildingFilter(0L);
		getUserPreferences().setApartmentFilter(0L);
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

}
