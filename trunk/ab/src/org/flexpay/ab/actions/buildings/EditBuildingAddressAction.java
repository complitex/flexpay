package org.flexpay.ab.actions.buildings;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class EditBuildingAddressAction extends FPActionSupport {

	private Long streetFilter;
	private Building building = Building.newInstance();
	private BuildingAddress address = new BuildingAddress();

	// type id to value mapping
	private Map<Long, String> attributeMap = CollectionUtils.treeMap();

	private String crumbCreateKey;
	private BuildingService buildingService;
	private StreetService streetService;
	private DistrictService districtService;
	private AddressAttributeTypeService addressAttributeTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (building.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_ERROR;
		}

		building = buildingService.read(stub(building));
		address = address.isNew() ? address : building.getAddress(stub(address));
		if (address == null) {
			log.warn("Building address mismatch: {}, {}", building, address);
			addActionError(getText("error.ab.internal.address_building_mismatch"));
			return REDIRECT_ERROR;
		}

		if (isNotSubmit()) {
			setupAttributes();
			return INPUT;
		}

		if (!doValidate()) {
			return INPUT;
		}

		Street street = streetService.readFull(new Stub<Street>(streetFilter));
		address.setStreet(street);
		for (Map.Entry<Long, String> attr : attributeMap.entrySet()) {
			AddressAttributeType type = addressAttributeTypeService.read(new Stub<AddressAttributeType>(attr.getKey()));
			address.setBuildingAttribute(attr.getValue(), type);
		}
		building.addAddress(address);

		log.debug("About to update building");
		buildingService.update(building);

		addActionError(getText("ab.building.saved"));

		return REDIRECT_SUCCESS;
	}

	private boolean doValidate() {

		boolean valid = true;

		Long buildingNumberAttributeId = ApplicationConfig.getBuildingAttributeTypeNumber().getId();
		if (StringUtils.isEmpty(attributeMap.get(buildingNumberAttributeId))) {
			addActionError(getText("ab.buildings.create.building_number_required"));
			valid = false;
		}

		if (streetFilter == null || streetFilter <= 0) {
			log.warn("Incorrect street id in filter ({})", streetFilter);
			addActionError(getText("ab.buildings.create.street_required"));
			valid = false;
		}

		return valid;
	}

	private void setupAttributes() {

		for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
			AddressAttribute attribute = address.getAttribute(type);
			attributeMap.put(type.getId(), attribute != null ? attribute.getValue() : "");
		}
		log.debug("Attributes: {}", attributeMap);

		District district = districtService.readFull(building.getDistrictStub());
		assert district != null;
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
		return INPUT;
	}

	@Override
	protected void setBreadCrumbs() {
		if (address.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public Long getStreetFilter() {
		return streetFilter;
	}

	public void setStreetFilter(Long streetFilter) {
		this.streetFilter = streetFilter;
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

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
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
