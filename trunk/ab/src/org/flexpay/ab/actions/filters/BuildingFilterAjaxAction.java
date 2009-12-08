package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.BuildingService;
import static org.flexpay.ab.util.TranslationUtil.getBuildingNumberWithoutHouseType;
import org.flexpay.ab.util.config.AbUserPreferences;
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

		if (parents == null) {
			log.warn("Parent parameter is null");
			addActionError(getText("common.error.invalid_id"));
			return SUCCESS;
		}

		Long streetId;

		try {
			streetId = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect street id in filter ({})", parents[0]);
			addActionError(getText("common.object_not_selected"));
			return SUCCESS;
		}
		if (streetId.equals(0L)) {
			return SUCCESS;
		}

		List<BuildingAddress> addresses = buildingService.findAddressesByParent(new Stub<Street>(streetId));
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

		filterString = "";

		if (filterValueLong == null || filterValueLong <= 0) {
			return;
		}

		BuildingAddress address = buildingService.readFullAddress(new Stub<BuildingAddress>(filterValueLong));
		if (address == null) {
			log.warn("Can't get building address with id {} from DB", filterValueLong);
			addActionError(getText("common.object_not_selected"));
			return;
		}

		filterString = getBuildingNumberWithoutHouseType(address.getBuildingAttributes(), getUserPreferences().getLocale());
	}

	@Override
	public void saveFilterValue() {

		if (filterString == null) {

			if (filterValueLong == null || filterValueLong <= 0) {
				log.warn("Incorrect filter value {}", filterValue);
				addActionError(getText("common.error.invalid_id"));
				return;
			}

			BuildingAddress address = buildingService.readFullAddress(new Stub<BuildingAddress>(filterValueLong));
			if (address == null) {
				log.warn("Can't get building address with id {} from DB", filterValueLong);
				addActionError(getText("common.object_not_selected"));
				return;
			}
		}

		AbUserPreferences up = getUserPreferences();
		up.setBuildingFilter(filterValueLong);
		up.setApartmentFilter(0L);
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
