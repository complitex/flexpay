package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.AddressAttribute;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuildingEditAction extends FPActionSupport {

	private BuildingAddress buildings = new BuildingAddress();
	private List<BuildingAddress> alternateBuildingsList = new ArrayList<BuildingAddress>();
	private Map<Long, String> attributeMap = CollectionUtils.treeMap();

	private BuildingService buildingService;
	private AddressAttributeTypeService addressAttributeTypeService;
	private AddressService addressService;

	public void prepareAttributes() {

		buildings = buildingService.readFull(stub(buildings));
		if (isNotSubmit()) {

			for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
				AddressAttribute attr = buildings.getAttribute(type);
				String value = "";
				if (attr != null) {
					value = attr.getValue();
				}
				attributeMap.put(type.getId(), value);
			}
		}
	}

	@NotNull
	public String doExecute() throws FlexPayException {

		if (buildings.isNew()) {
			log.info("No buildings id specified to edit");
			return REDIRECT_SUCCESS;
		}

		prepareAttributes();

		for (BuildingAddress current : buildingService.getBuildingBuildings(buildings.getBuildingStub())) {
			if (!buildings.equals(current)) {
				alternateBuildingsList.add(buildingService.readFull(stub(current)));
			}
		}

		if (isSubmit()) {
			for (Long typeId : attributeMap.keySet()) {
				AddressAttributeType type = addressAttributeTypeService.read(new Stub<AddressAttributeType>(typeId));
				buildings.setBuildingAttribute(attributeMap.get(typeId), type);
			}

			buildingService.update(buildings);
		}

		return INPUT;
	}

	public String getAddress(@NotNull Long buildingsId) throws Exception {
		return addressService.getBuildingsAddress(new Stub<BuildingAddress>(buildingsId), getUserPreferences().getLocale());
	}

	public String getTypeName(Long typeId) throws FlexPayException {
		AddressAttributeType type = addressAttributeTypeService.read(new Stub<AddressAttributeType>(typeId));
		if (type == null) {
			throw new RuntimeException("Unknown type id: " + typeId);
		}
		return getTranslation(type.getTranslations()).getName();
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

	public BuildingAddress getBuildings() {
		return buildings;
	}

	public void setBuildings(BuildingAddress buildingAddress) {
		this.buildings = buildingAddress;
	}

	public List<BuildingAddress> getAlternateBuildingsList() {
		return alternateBuildingsList;
	}

	public Map<Long, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<Long, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	@Required
	public void setBuildingAttributeTypeService(AddressAttributeTypeService addressAttributeTypeService) {
		this.addressAttributeTypeService = addressAttributeTypeService;
	}

}
