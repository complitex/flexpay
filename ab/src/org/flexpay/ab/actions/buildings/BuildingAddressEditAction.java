package org.flexpay.ab.actions.buildings;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
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
	private StreetService streetService;
	private TownService townService;
	private RegionService regionService;
	private DistrictService districtService;
	private AddressAttributeTypeService addressAttributeTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (building.isNew()) {
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		building = buildingService.readFull(stub(building));
		if (building == null) {
			log.error(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		if (address.getId() == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		address = address.isNew() ? address : building.getAddress(stub(address));
		if (address == null) {
			log.warn("Building address mismatch: {}, {}", building, address);
			addActionError(getText("error.ab.internal.address_building_mismatch"));
			return REDIRECT_ERROR;
		}

		if (address.isNotNew()) {
			setupFilters();
		}

		if (isNotSubmit()) {
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

	private void setupFilters() {

		Street street = streetService.readFull(address.getStreetStub());
		if (street == null) {
			log.warn("Can't get street with id {} from DB", address.getStreetStub().getId());
			return;
		}
		Town town = townService.readFull(street.getTownStub());
		if (town == null) {
			log.warn("Can't get town with id {} from DB", street.getTownStub().getId());
			return;
		}
		Region region = regionService.readFull(town.getRegionStub());
		if (region == null) {
			log.warn("Can't get region with id {} from DB", town.getRegionStub().getId());
			return;
		}

		streetFilter = address.getStreetStub().getId();
		townFilter = town.getId();
		regionFilter = region.getId();
		countryFilter = region.getCountryStub().getId();
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
		if (address.isNew()) {
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
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
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
