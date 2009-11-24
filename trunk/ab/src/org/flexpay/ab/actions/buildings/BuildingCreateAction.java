package org.flexpay.ab.actions.buildings;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.ObjectsFactory;
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

	private BuildingService buildingService;
	private ObjectsFactory objectsFactory;
	private AddressAttributeTypeService addressAttributeTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (attributesMap == null) {
			log.debug("Incorrect attributesMap parameter (null)");
			attributesMap = treeMap();
		}

		if (isNotSubmit()) {
			setupAttributes();
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

		boolean valid = true;

		if (districtFilter == null || districtFilter <= 0) {
			log.warn("Incorrect district id in filter ({})", districtFilter);
			addActionError(getText("ab.error.building_address.district_required"));
			valid = false;
		}
		if (streetFilter == null || streetFilter <= 0) {
			log.warn("Incorrect street id in filter ({})", streetFilter);
			addActionError(getText("ab.error.building_address.street_required"));
			valid = false;
		}

		Long buildingNumberAttributeId = ApplicationConfig.getBuildingAttributeTypeNumber().getId();
		if (StringUtils.isEmpty(attributesMap.get(buildingNumberAttributeId))) {
			log.warn("Required building attribute not set");
			addActionError(getText("ab.error.building_address.building_number_required"));
			valid = false;
		}

		return valid;
	}

	private void setupAttributes() {
		for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
			attributesMap.put(type.getId(), "");
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

}
