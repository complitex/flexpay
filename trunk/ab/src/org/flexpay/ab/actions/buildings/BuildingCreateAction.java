package org.flexpay.ab.actions.buildings;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

/**
 * Create building with a single required address
 */
public class BuildingCreateAction extends FPActionSupport {

	private Long streetFilter;
	private Long districtFilter;

	private Map<Long, String> attributesMap = treeMap();
	private Building building;

	private ObjectsFactory objectsFactory;
	private AddressAttributeTypeService addressAttributeTypeService;
	private BuildingService buildingService;
	private StreetService streetService;
	private DistrictService districtService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		correctAttributes();

		if (isNotSubmit()) {
			return INPUT;
		}

		if (!doValidate()) {
			return INPUT;
		}

		building = objectsFactory.newBuilding();
		building.setDistrict(new District(districtFilter));

		BuildingAddress address = new BuildingAddress();
		address.setPrimaryStatus(true);
		address.setStreet(new Street(streetFilter));
		for (Map.Entry<Long, String> attr : attributesMap.entrySet()) {
			AddressAttributeType type = addressAttributeTypeService.readFull(new Stub<AddressAttributeType>(attr.getKey()));
			address.setBuildingAttribute(attr.getValue(), type);
		}
		building.addAddress(address);

		buildingService.create(building);

		addActionMessage(getText("ab.building.saved"));

		return REDIRECT_SUCCESS;
	}

	private boolean doValidate() {

		if (districtFilter == null || districtFilter <= 0) {
			log.warn("Incorrect district id in filter ({})", districtFilter);
			addActionError(getText("ab.error.building_address.district_required"));
		} else {
			Stub<District> stub = new Stub<District>(districtFilter);
			District district = districtService.readFull(stub);
			if (district == null) {
				log.warn("Can't get district with id {} from DB", stub.getId());
				addActionError(getText("common.object_not_selected"));
			} else if (district.isNotActive()) {
				log.warn("District with id {} is disabled", stub.getId());
				addActionError(getText("common.object_not_selected"));
			}
		}

		if (streetFilter == null || streetFilter <= 0) {
			log.warn("Incorrect street id in filter ({})", streetFilter);
			addActionError(getText("ab.error.building_address.street_required"));
		} else {
			Stub<Street> stub = new Stub<Street>(streetFilter);
			Street street = streetService.readFull(stub);
			if (street == null) {
				log.warn("Can't get street with id {} from DB", stub.getId());
				addActionError(getText("common.object_not_selected"));
			} else if (street.isNotActive()) {
				log.warn("Street with id {} is disabled", stub.getId());
				addActionError(getText("common.object_not_selected"));
			}
		}

		Long buildingNumberAttributeId = ApplicationConfig.getBuildingAttributeTypeNumber().getId();
		if (StringUtils.isEmpty(attributesMap.get(buildingNumberAttributeId))) {
			log.warn("Required building attribute not set");
			addActionError(getText("ab.error.building_address.building_number_required"));
		}

		return !hasActionErrors();
	}

	private void correctAttributes() {
		if (attributesMap == null) {
			log.debug("AttributesMap parameter is null");
			attributesMap = treeMap();
		}
		Map<Long, String> newAttributesMap = treeMap();
		for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
			newAttributesMap.put(type.getId(), attributesMap.containsKey(type.getId()) ? attributesMap.get(type.getId()) : "");
		}
		attributesMap = newAttributesMap;
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

	public String getTypeName(Long typeId) throws FlexPayException {
		AddressAttributeType type = addressAttributeTypeService.readFull(new Stub<AddressAttributeType>(typeId));
		if (type == null) {
			throw new RuntimeException("Unknown type id: " + typeId);
		}
		return getTranslationName(type.getTranslations());
	}

	public Long getStreetFilter() {
		return streetFilter;
	}

	public void setStreetFilter(Long streetFilter) {
		this.streetFilter = streetFilter;
	}

	public Long getDistrictFilter() {
		return districtFilter;
	}

	public void setDistrictFilter(Long districtFilter) {
		this.districtFilter = districtFilter;
	}

	public Map<Long, String> getAttributesMap() {
		return attributesMap;
	}

	public void setAttributesMap(Map<Long, String> attributesMap) {
		this.attributesMap = attributesMap;
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
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}
}
