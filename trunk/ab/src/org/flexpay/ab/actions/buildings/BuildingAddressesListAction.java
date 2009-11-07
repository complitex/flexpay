package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.DomainObject.collectionIds;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class BuildingAddressesListAction extends FPActionSupport {

	private Building building = Building.newInstance();
	private List<BuildingAddress> addresses = list();

	private BuildingService buildingService;
	private AddressService addressService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (building.isNew()) {
			addActionError(getText("error.no_id"));
			return SUCCESS;
		}

		building = buildingService.readFull(stub(building));
		if (building == null) {
			log.error(getText("common.object_not_selected"));
			return SUCCESS;
		}
		addresses = buildingService.readFullAddresses(collectionIds(building.getBuildingses()), true);
		if (log.isDebugEnabled()) {
			log.debug("Total building addresses found: {}", addresses.size());
		}

		return SUCCESS;
	}

	@NotNull
	public String getAddress(@NotNull Long addressId) throws Exception {
		return addressService.getBuildingsAddress(new Stub<BuildingAddress>(addressId), getLocale());
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public List<BuildingAddress> getAddresses() {
		return addresses;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

}
