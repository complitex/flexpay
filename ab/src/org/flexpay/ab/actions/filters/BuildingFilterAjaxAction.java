package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.util.TranslationUtil;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class BuildingFilterAjaxAction extends FilterAjaxAction {

	private BuildingService buildingService;

	@NotNull
	public String doExecute() throws FlexPayException {

		Long streetIdLong;

		try {
			streetIdLong = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect street id in filter ({})", parents[0]);
			return SUCCESS;
		}

		List<BuildingAddress> addresses = buildingService.getBuildings(new Stub<Street>(streetIdLong));
		log.debug("Found addresses: {}", addresses);

		foundObjects = new ArrayList<FilterObject>();
		for (BuildingAddress address : addresses) {
			FilterObject object = new FilterObject();
			object.setValue(address.getId() + "");
			object.setName(TranslationUtil.getBuildingNumberWithoutHouseType(
					address.getBuildingAttributes(), getUserPreferences().getLocale()));
			foundObjects.add(object);
		}

		return SUCCESS;
	}

	public void readFilterString() throws FlexPayException {
		if (filterValueLong != null) {
			BuildingAddress address = buildingService.readFull(new Stub<BuildingAddress>(filterValueLong));
			if (address != null) {
				filterString = TranslationUtil.getBuildingNumberWithoutHouseType(
						address.getBuildingAttributes(), getUserPreferences().getLocale());
			} else {
				filterString = "";
			}
		} else {
			filterString = "";
		}
	}

	public void saveFilterValue() {
		getUserPreferences().setBuildingFilterValue(filterValue);
		getUserPreferences().setApartmentFilterValue("");
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
