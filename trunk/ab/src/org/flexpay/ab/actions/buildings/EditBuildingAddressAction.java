package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.exception.FlexPayException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class EditBuildingAddressAction extends FPActionSupport {

	private TownFilter townFilter = new TownFilter();
	private StreetFilter streetFilter = new StreetFilter();
	private Building building = Building.newInstance();
	private BuildingAddress address = new BuildingAddress();

	// type id to value mapping
	private Map<Long, String> attributeMap = CollectionUtils.treeMap();

	private BuildingService buildingService;
	private StreetService streetService;
	private DistrictService districtService;
	private AddressAttributeTypeService addressAttributeTypeService;

	{
		townFilter.setReadOnly(true);
	}

	@NotNull
	public String doExecute() throws Exception {

		if (building.getId() == null || address.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_ERROR;
		}

		building = buildingService.read(stub(building));
		BuildingAddress addr = address.isNew() ? address : building.getAddress(stub(address));
		if (addr == null) {
			log.warn("Building address mismatch: {}, {}", building, address);
			addActionError(getText("error.ab.internal.address_building_mismatch"));
			return REDIRECT_ERROR;
		}

		if (!isSubmit()) {
			address = addr;
			setupAttributes();
			return INPUT;
		}

		boolean valid = true;
		if (!streetFilter.needFilter()) {
			addActionError(getText("ab.buildings.create.street_required"));
			valid = false;
		}
		if (attributeMap.isEmpty()) {
			addActionError(getText("ab.buildings.create.buildings_attr_required"));
			valid = false;
		}
		if (!valid) {
			return INPUT;
		}

		Street street = streetService.readFull(streetFilter.getSelectedStub());
		addr.setStreet(street);
		for (Map.Entry<Long, String> attr : attributeMap.entrySet()) {
			AddressAttributeType type = addressAttributeTypeService.read(new Stub<AddressAttributeType>(attr.getKey()));
			addr.setBuildingAttribute(attr.getValue(), type);
		}
		if (addr.isNew()) {
			addr.setId(null);
		}
		building.addAddress(addr);

		log.debug("About to update building");
		buildingService.update(building);

		addActionError(getText("ab.building.saved"));

		return REDIRECT_SUCCESS;
	}

	private void setupAttributes() {

		for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
			AddressAttribute attribute = address.getAttribute(type);
			attributeMap.put(type.getId(), attribute != null ? attribute.getValue() : "");
		}
		log.debug("Attributes: {}", attributeMap);

		if (address.isNotNew()) {
			streetFilter.setSelected(address.getStreetStub());
		}

		District district = districtService.readFull(building.getDistrictStub());
		assert district != null;
		townFilter.setSelected(district.getTownStub());
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return INPUT;
	}

	public StreetFilter getStreetFilter() {
		return streetFilter;
	}

	public void setStreetFilter(StreetFilter streetFilter) {
		this.streetFilter = streetFilter;
	}

	public TownFilter getTownFilter() {
		return townFilter;
	}

	public void setTownFilter(TownFilter townFilter) {
		this.townFilter = townFilter;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public BuildingAddress getAddress() {
		return address;
	}

	public void setAddress(BuildingAddress address) {
		this.address = address;
	}

	public Map<Long, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<Long, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public String getTypeName(Long typeId) throws FlexPayException {
		AddressAttributeType type = addressAttributeTypeService.read(new Stub<AddressAttributeType>(typeId));
		if (type == null) {
			throw new RuntimeException("Unknown type id: " + typeId);
		}
		return getTranslation(type.getTranslations()).getName();
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setAddressAttributeTypeService(AddressAttributeTypeService addressAttributeTypeService) {
		this.addressAttributeTypeService = addressAttributeTypeService;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}
}