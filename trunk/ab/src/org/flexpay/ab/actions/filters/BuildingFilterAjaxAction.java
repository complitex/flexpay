package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.UserPreferences;
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
			object.setName(getBuildingNumber(address.getBuildingAttributes()));
			foundObjects.add(object);
		}

		return SUCCESS;
	}

	public void readFilterString() {
		if (filterValueLong != null) {
			BuildingAddress address = buildingService.readFull(new Stub<BuildingAddress>(filterValueLong));
			if (address != null) {
				filterString = getBuildingNumber(address.getBuildingAttributes());
			} else {
				filterString = "";
			}
		} else {
			filterString = "";
		}
	}

	public void saveFilterValue() {
		UserPreferences prefs = UserPreferences.getPreferences(request);
		prefs.setBuildingFilterValue(filterValue);
		prefs.setApartmentFilterValue("");
		UserPreferences.setPreferences(request, prefs);
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
