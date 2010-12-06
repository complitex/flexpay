package org.flexpay.ab.action.filter;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.filter.FilterAjaxAction;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.set;

import org.flexpay.common.persistence.filter.FilterObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Set;

/**
 * Search districts by name
 */
public class DistrictFilterAjaxAction extends FilterAjaxAction {

	private DistrictService districtService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		if (parents == null) {
			log.warn("Parent parameter is null");
			addActionError(getText("common.error.invalid_id"));
			return SUCCESS;
		}

		Long townId;

		try {
			townId = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect town id in filter ({})", parents[0]);
			addActionError(getText("ab.error.town.incorrect_town_id"));
			return SUCCESS;
		}
		if (townId.equals(0L)) {
			return SUCCESS;
		}

		if (q == null) {
			q = "";
		}

		List<District> districts = districtService.findByParentAndQuery(new Stub<Town>(townId), "%" + q + "%");
		if (log.isDebugEnabled()) {
			log.debug("Found districts: {}", districts.size());
		}

		Set<Long> districtIds = set();

		for (District district : districts) {
			districtIds.add(district.getId());
		}

		districts = districtService.readFull(districtIds, true);

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

		filterString = "";

		if (filterValueLong == null || filterValueLong <= 0) {
			return;
		}

		District district = districtService.readFull(new Stub<District>(filterValueLong));
		if (district == null) {
			log.warn("Can't get district with id {} from DB", filterValueLong);
			addActionError(getText("ab.error.district.cant_get_district"));
			return;
		}
		if (district.getCurrentName() != null) {
			filterString = getTranslationName(district.getCurrentName().getTranslations());
		}
	}

	@Override
	public void saveFilterValue() {

		if (filterValueLong == null || filterValueLong <= 0) {
			log.warn("Incorrect filter value {}", filterValue);
			addActionError(getText("ab.error.district.incorrect_district_id"));
			return;
		}

		District district = districtService.readFull(new Stub<District>(filterValueLong));
		if (district == null) {
			log.warn("Can't get district with id {} from DB", filterValueLong);
			addActionError(getText("ab.error.district.cant_get_district"));
			return;
		} else if (district.isNotActive()) {
			log.warn("District with id {} is disabled", filterValueLong);
			addActionError(getText("ab.error.district.cant_get_district"));
			return;
		}

		AbUserPreferences up = getUserPreferences();
		up.setDistrictFilter(filterValueLong);
		up.setBuildingFilter(0L);
		up.setApartmentFilter(0L);
	}

    @Override
    public AbUserPreferences getUserPreferences() {
        return (AbUserPreferences) super.getUserPreferences();
    }

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

}
