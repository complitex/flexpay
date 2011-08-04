package org.flexpay.ab.action.building;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;

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
	private StreetService streetService;
	private DistrictService districtService;
	private AddressAttributeTypeService addressAttributeTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (building == null || building.isNew()) {
			log.warn("Incorrect building id");
			addActionError(getText("ab.error.building.incorrect_building_id"));
			return REDIRECT_ERROR;
		}

		Stub<Building> buildingStub = stub(building);
		building = buildingService.readFull(buildingStub);

		if (building == null) {
			log.warn("Can't get building with id {} from DB", buildingStub.getId());
			addActionError(getText("ab.error.building.cant_get_building"));
			return REDIRECT_ERROR;
		} else if (building.isNotActive()) {
			log.warn("Building with id {} is disabled", buildingStub.getId());
			addActionError(getText("ab.error.building.cant_get_building"));
			return REDIRECT_ERROR;
		}

		if (address == null || address.getId() == null) {
			log.warn("Incorrect building address id");
			addActionError(getText("ab.error.building_address.incorrect_address_id"));
			return REDIRECT_ERROR;
		}

		if (address.isNotNew()) {
			Stub<BuildingAddress> stub = stub(address);
			address = building.getAddress(stub);

			if (address == null) {
				log.warn("Building address mismatch: {}, {}", building, address);
				addActionError(getText("ab.error.building_address.cant_get_address"));
				return REDIRECT_ERROR;
			} else if (address.isNotActive()) {
				log.warn("Building address with id {} is disabled", stub.getId());
				addActionError(getText("ab.error.building_address.cant_get_address"));
				return REDIRECT_ERROR;
			}

		}

        correctAttributes();

		if (isNotSubmit()) {

			if (address.isNotNew()) {
				Stub<BuildingAddress> stub = stub(address);
				address = buildingService.readWithHierarchy(stub);
				if (address == null) {
					log.warn("Can't get building address with id {} from DB", stub.getId());
					addActionError(getText("ab.error.building_address.cant_get_address"));
					return REDIRECT_ERROR;
				}
				streetFilter = address.getStreet().getId();
				townFilter = address.getTown().getId();
				regionFilter = address.getRegion().getId();
				countryFilter = address.getCountry().getId();
			}

            for (AddressAttribute attribute : address.getBuildingAttributes()) {
                attributesMap.put(attribute.getBuildingAttributeType().getId(), attribute.getValue());
            }

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

        if (address.isNew()) {
            buildingService.createAddress(address, building);
        } else {
            buildingService.updateAddress(address, building);
        }

		addActionMessage(getText("ab.building.saved"));

		return REDIRECT_SUCCESS;
	}

	private boolean doValidate() {

		Long buildingNumberAttributeId = ApplicationConfig.getBuildingAttributeTypeNumber().getId();
		if (StringUtils.isEmpty(attributesMap.get(buildingNumberAttributeId))) {
			log.warn("Building number attribute not set");
			addActionError(getText("ab.error.building_address.building_number_required"));
		}

		if (streetFilter == null || streetFilter <= 0) {
			log.warn("Incorrect street id in filter ({})", streetFilter);
			addActionError(getText("ab.error.street.incorrect_street_id"));
			streetFilter = 0L;
		} else {
			Street street = streetService.readFull(new Stub<Street>(streetFilter));
			if (street == null) {
				log.warn("Can't get street with id {} from DB", streetFilter);
				addActionError(getText("ab.error.street.cant_get_street"));
				streetFilter = 0L;
			} else if (street.isNotActive()) {
				log.warn("Street with id {} is disabled", streetFilter);
				addActionError(getText("ab.error.street.cant_get_street"));
				streetFilter = 0L;
			}
		}

		return !hasActionErrors();
	}

	private void correctAttributes() {
		if (attributesMap == null) {
			log.warn("AttributesMap parameter is null");
			attributesMap = treeMap();
		}
		Map<Long, String> newAttributesMap = treeMap();
		for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
			newAttributesMap.put(type.getId(), attributesMap.containsKey(type.getId()) ? attributesMap.get(type.getId()) : "");
		}
		attributesMap = newAttributesMap;

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

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}
}
