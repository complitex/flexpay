package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.BuildingAttribute;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingAttributeTypeService;
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

	private BuildingService buildingService;
	private BuildingAttributeTypeService buildingAttributeTypeService;
	private AddressService addressService;

	private Buildings buildings = new Buildings();
	private List<Buildings> alternateBuildingsList = new ArrayList<Buildings>();
	private Map<Long, String> attributeMap = CollectionUtils.treeMap();

	public void prepareAttributes() {

		buildings = buildingService.readFull(stub(buildings));
		if (isNotSubmit()) {

			for (BuildingAttributeType type : buildingAttributeTypeService.getAttributeTypes()) {
				BuildingAttribute attr = buildings.getAttribute(type);
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

		for (Buildings current : buildingService.getBuildingBuildings(buildings.getBuildingStub())) {
			if (!buildings.equals(current)) {
				alternateBuildingsList.add(buildingService.readFull(stub(current)));
			}
		}

		if (isSubmit()) {
			for (Long typeId : attributeMap.keySet()) {
				BuildingAttributeType type = buildingAttributeTypeService.read(new Stub<BuildingAttributeType>(typeId));
				buildings.setBuildingAttribute(attributeMap.get(typeId), type);
			}

			buildingService.update(buildings);
		}

		return INPUT;
	}

	public String getAddress(@NotNull Long buildingsId) throws Exception {
		return addressService.getBuildingsAddress(new Stub<Buildings>(buildingsId), getUserPreferences().getLocale());
	}

	public String getTypeName(Long typeId) throws FlexPayException {
		BuildingAttributeType type = buildingAttributeTypeService.read(new Stub<BuildingAttributeType>(typeId));
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

	/**
	 * @return the buildings
	 */
	public Buildings getBuildings() {
		return buildings;
	}

	/**
	 * @param buildings the buildings to set
	 */
	public void setBuildings(Buildings buildings) {
		this.buildings = buildings;
	}

	/**
	 * @return the alternateBuildingsList
	 */
	public List<Buildings> getAlternateBuildingsList() {
		return alternateBuildingsList;
	}

	/**
	 * @return the attributeMap
	 */
	public Map<Long, String> getAttributeMap() {
		return attributeMap;
	}

	/**
	 * @param attributeMap the attributeMap to set
	 */
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
	public void setBuildingAttributeTypeService(BuildingAttributeTypeService buildingAttributeTypeService) {
		this.buildingAttributeTypeService = buildingAttributeTypeService;
	}
}
