package org.flexpay.ab.actions.buildings;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class BuildingAddressEditAction extends FPActionSupport {

	private Long countryFilter;
	private Long regionFilter;
	private Long townFilter;
	private Long streetFilter;

	private Building building = Building.newInstance();
	private BuildingAddress address = new BuildingAddress();

	private Map<Long, String> attributesMap = treeMap();

	private String crumbCreateKey;
	private BuildingService buildingService;
	private DistrictService districtService;
	private AddressAttributeTypeService addressAttributeTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (building == null || building.getId() == null) {
			log.debug("Incorrect building id");
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		if (building.isNotNew()) {
			Stub<Building> stub = stub(building);
			building = buildingService.readFull(stub);

			if (building == null) {
				log.debug("Can't get building with id {} from DB", stub.getId());
				addActionError(getText("common.object_not_selected"));
				return REDIRECT_ERROR;
			} else if (building.isNotActive()) {
				log.debug("Building with id {} is disabled", stub.getId());
				addActionError(getText("common.object_not_selected"));
				return REDIRECT_ERROR;
			}

		}

		if (address == null || address.getId() == null) {
			log.debug("Incorrect building address id");
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		if (address.isNotNew()) {
			Stub<BuildingAddress> stub = stub(address);
			address = building.getAddress(stub);

			if (address == null) {
				log.debug("Building address mismatch: {}, {}", building, address);
				addActionError(getText("common.object_not_selected"));
				return REDIRECT_ERROR;
			} else if (address.isNotActive()) {
				log.debug("Building address with id {} is disabled", stub.getId());
				addActionError(getText("common.object_not_selected"));
				return REDIRECT_ERROR;
			}

		}

		if (attributesMap == null) {
			log.debug("AttributesMap parameter is null");
			attributesMap = treeMap();
		}

		if (isNotSubmit()) {

			if (address.isNotNew()) {
				Stub<BuildingAddress> stub = stub(address);
				address = buildingService.readWithHierarchy(stub);
				if (address == null) {
					log.debug("Can't get building address with id {} from DB", stub.getId());
					addActionError(getText("common.object_not_selected"));
					return REDIRECT_ERROR;
				}
				streetFilter = address.getStreet().getId();
				townFilter = address.getTown().getId();
				regionFilter = address.getRegion().getId();
				countryFilter = address.getCountry().getId();
			}

			setupAttributes();
			return INPUT;
		}

		if (!doValidate()) {
			return INPUT;
		}

		address.setStreet(new Street(streetFilter));
		for (Map.Entry<Long, String> attr : attributesMap.entrySet()) {
			AddressAttributeType type = addressAttributeTypeService.readFull(new Stub<AddressAttributeType>(attr.getKey()));
			address.setBuildingAttribute(attr.getValue(), type);
		}
		building.addAddress(address);

		buildingService.update(building);

		addActionMessage(getText("ab.building.saved"));

		return REDIRECT_SUCCESS;
	}

	private boolean doValidate() {

		boolean valid = true;

		Long buildingNumberAttributeId = ApplicationConfig.getBuildingAttributeTypeNumber().getId();
		if (StringUtils.isEmpty(attributesMap.get(buildingNumberAttributeId))) {
			addActionError(getText("ab.error.building_address.building_number_required"));
			valid = false;
		}

		if (streetFilter == null || streetFilter <= 0) {
			log.warn("Incorrect street id in filter ({})", streetFilter);
			addActionError(getText("ab.error.building_address.street_required"));
			valid = false;
		}

		return valid;
	}

	private void setupAttributes() {

		for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
			AddressAttribute attribute = address.getAttribute(type);
			attributesMap.put(type.getId(), attribute != null ? attribute.getValue() : "");
		}
		log.debug("Address attributes: {}", attributesMap);

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
		if (address != null && address.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public String getTypeName(Long typeId) throws FlexPayException {
		AddressAttributeType type = addressAttributeTypeService.readFull(new Stub<AddressAttributeType>(typeId));
		if (type == null) {
			throw new RuntimeException("Unknown address attribute type id: " + typeId);
		}
		return getTranslation(type.getTranslations()).getName();
	}

	public Long getCountryFilter() {
		return countryFilter;
	}

	public Long getRegionFilter() {
		return regionFilter;
	}

	public Long getTownFilter() {
		return townFilter;
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

	public Map<Long, String> getAttributesMap() {
		return attributesMap;
	}

	public void setAttributesMap(Map<Long, String> attributesMap) {
		this.attributesMap = attributesMap;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
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
