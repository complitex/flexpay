package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.BuildingService;
import static org.flexpay.ab.util.TranslationUtil.getBuildingNumberWithoutHouseType;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class BuildingFilterAjaxAction extends FilterAjaxAction {

	private BuildingService buildingService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		Long streetId;

		try {
			streetId = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect street id in filter ({})", parents[0]);
			return SUCCESS;
		}
		if (streetId == 0) {
			return SUCCESS;
		}

		List<BuildingAddress> addresses = buildingService.getBuildings(new Stub<Street>(streetId));
		if (log.isDebugEnabled()) {
			log.debug("Found addresses: {}", addresses.size());
		}

		for (BuildingAddress address : addresses) {
			FilterObject object = new FilterObject();
			object.setValue(address.getId() + "");
			object.setName(getBuildingNumberWithoutHouseType(address.getBuildingAttributes(), getUserPreferences().getLocale()));
			foundObjects.add(object);
		}

		return SUCCESS;
	}

	@Override
	public void readFilterString() throws FlexPayException {
		if (filterValueLong != null && filterValueLong > 0) {
			BuildingAddress address = buildingService.readFull(new Stub<BuildingAddress>(filterValueLong));
			if (address != null) {
				filterString = getBuildingNumberWithoutHouseType(address.getBuildingAttributes(), getUserPreferences().getLocale());
			} else {
				filterString = "";
			}
		} else {
			filterString = "";
		}
	}

	@Override
	public void saveFilterValue() {
		getUserPreferences().setBuildingFilter(filterValueLong);
		getUserPreferences().setApartmentFilter(0L);
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
