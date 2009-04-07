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

		if (saveFilterValue()) {
			return SUCCESS;
		}

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

	public boolean saveFilterValue() {
		if (filterValue != null) {
			try {
				UserPreferences prefs = UserPreferences.getPreferences(request);
				prefs.setBuildingFilterValue(Long.parseLong(filterValue));
				UserPreferences.setPreferences(request, prefs);
				return true;
			} catch (Exception e) {
				log.warn("Incorrect country id in filter ({})", filterValue);
			}
		}
		return false;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
