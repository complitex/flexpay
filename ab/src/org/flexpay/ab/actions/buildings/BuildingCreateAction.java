package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.DistrictFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.service.*;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

/**
 * Create building with a single required address
 */
public class BuildingCreateAction extends FPActionSupport {

	private StreetFilter streetFilter = new StreetFilter();
	private DistrictFilter districtFilter = new DistrictFilter();

	// type id to value mapping
	private Map<Long, String> attributeMap = CollectionUtils.treeMap();

	private BuildingService buildingService;
	private DistrictService districtService;
	private StreetService streetService;
	private ObjectsFactory objectsFactory;
	private AddressAttributeTypeService addressAttributeTypeService;

	private Building building;

	@NotNull
	public String doExecute() throws Exception {

		if (!isSubmit()) {
			setupAttributes();
			return INPUT;
		}

		boolean valid = true;
		if (!districtFilter.needFilter()) {
			addActionError(getText("ab.buildings.create.district_required"));
			valid = false;
		}
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

		building = objectsFactory.newBuilding();

		District district = districtService.readFull(districtFilter.getSelectedStub());
		building.setDistrict(district);

		BuildingAddress address = new BuildingAddress();
		address.setPrimaryStatus(true);
		Street street = streetService.readFull(streetFilter.getSelectedStub());
		address.setStreet(street);
		for (Map.Entry<Long, String> attr : attributeMap.entrySet()) {
			AddressAttributeType type = addressAttributeTypeService.read(new Stub<AddressAttributeType>(attr.getKey()));
			address.setBuildingAttribute(attr.getValue(), type);
		}
		building.addAddress(address);

		log.debug("About to save new building");
		buildingService.create(building);

		addActionError(getText("ab.building.saved"));

		return REDIRECT_SUCCESS;
	}

	private void setupAttributes() {

		log.debug("Attributes: {}", attributeMap);

		for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
			attributeMap.put(type.getId(), "");
		}
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

	public String getTypeName(Long typeId) throws FlexPayException {
		AddressAttributeType type = addressAttributeTypeService.read(new Stub<AddressAttributeType>(typeId));
		if (type == null) {
			throw new RuntimeException("Unknown type id: " + typeId);
		}
		return getTranslation(type.getTranslations()).getName();
	}

	public StreetFilter getStreetFilter() {
		return streetFilter;
	}

	public void setStreetFilter(StreetFilter streetFilter) {
		this.streetFilter = streetFilter;
	}

	public DistrictFilter getDistrictFilter() {
		return districtFilter;
	}

	public void setDistrictFilter(DistrictFilter districtFilter) {
		this.districtFilter = districtFilter;
	}

	public Map<Long, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<Long, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public Building getBuilding() {
		return building;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setObjectsFactory(ObjectsFactory objectsFactory) {
		this.objectsFactory = objectsFactory;
	}

	@Required
	public void setAddressAttributeTypeService(AddressAttributeTypeService addressAttributeTypeService) {
		this.addressAttributeTypeService = addressAttributeTypeService;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}
}
