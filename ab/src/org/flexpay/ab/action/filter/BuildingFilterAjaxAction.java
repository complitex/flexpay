package org.flexpay.ab.action.filter;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.BuildingService;
import static org.flexpay.ab.util.TranslationUtil.getBuildingNumberWithoutHouseType;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.action.filter.FilterAjaxAction;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.set;

import org.flexpay.common.persistence.filter.FilterObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Set;

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
			addActionError(getText("ab.error.street.incorrect_street_id"));
			return SUCCESS;
		}
		if (streetId.equals(0L)) {
			return SUCCESS;
		}

		List<BuildingAddress> addresses = buildingService.findAddressesByParent(new Stub<Street>(streetId));
		if (log.isDebugEnabled()) {
			log.debug("Found addresses: {}", addresses.size());
		}

		Set<Long> addressIds = set();

		for (BuildingAddress address : addresses) {
			addressIds.add(address.getId());
		}

		addresses = buildingService.readFullAddresses(addressIds, true);

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
			addActionError(getText("ab.error.building_address.cant_get_address"));
			return;
		}

		filterString = getBuildingNumberWithoutHouseType(address.getBuildingAttributes(), getUserPreferences().getLocale());
	}

	@Override
	public void saveFilterValue() {

		if (filterValueLong == null || filterValueLong <= 0) {
			log.warn("Incorrect filter value {}", filterValue);
			addActionError(getText("ab.error.building_address.incorrect_address_id"));
			return;
		}

		BuildingAddress address = buildingService.readFullAddress(new Stub<BuildingAddress>(filterValueLong));
		if (address == null) {
			log.warn("Can't get building address with id {} from DB", filterValueLong);
			addActionError(getText("ab.error.building_address.cant_get_address"));
			return;
		} else if (address.isNotActive()) {
			log.warn("Building address with id {} is disabled", filterValueLong);
			addActionError(getText("ab.error.building_address.cant_get_address"));
			return;
		}

		AbUserPreferences up = getUserPreferences();
		up.setBuildingFilter(filterValueLong);
		up.setApartmentFilter(0L);
	}

    @Override
    public AbUserPreferences getUserPreferences() {
        return (AbUserPreferences) super.getUserPreferences();
    }

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
