package org.flexpay.tc.actions.buildingattributes;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class BuildingAttributesEditPageAction extends FPActionSupport {

	private BuildingAddress building = new BuildingAddress();
	private List<BuildingAddress> alternateAddresses = CollectionUtils.list();

	private Date attributeDate = DateUtil.now();

	private AddressService addressService;
	private BuildingService buildingService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		Stub<BuildingAddress> stub = stub(building);

		building = buildingService.readFullAddress(stub);
		if (building == null) {
			throw new FlexPayException("No building with id " + stub.getId() + " can be retrieved");
		}

		Set<Long> ids = CollectionUtils.set();

		for (BuildingAddress address : buildingService.findAddresesByBuilding(building.getBuildingStub())) {
			if (!building.equals(address)) {
				ids.add(address.getId());
			}
		}
		alternateAddresses.addAll(buildingService.readFullAddresses(ids, false));

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public String getAddress(@NotNull Long buildingId) throws Exception {
		return addressService.getBuildingsAddress(new Stub<BuildingAddress>(buildingId), getUserPreferences().getLocale());
	}

	public BuildingAddress getBuilding() {
		return building;
	}

	public void setBuilding(BuildingAddress building) {
		this.building = building;
	}

	public List<BuildingAddress> getAlternateAddresses() {
		return alternateAddresses;
	}

	public String getAttributeDate() {
		return format(attributeDate);
	}

	@Required
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
