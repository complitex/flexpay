package org.flexpay.ab.actions.buildings;

import com.opensymphony.xwork2.Preparable;
import org.flexpay.ab.persistence.BuildingAttribute;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildingsEditAction extends FPActionSupport implements Preparable {

	private BuildingService buildingService;

	private Buildings buildings = new Buildings();
	private List<Buildings> alternateBuildingsList = new ArrayList<Buildings>();
	private Map<String, BuildingAttribute> attributeMap;

	public void prepare() {
		buildings = buildingService.readFull(stub(buildings));

		attributeMap = new HashMap<String, BuildingAttribute>();
		for (BuildingAttributeType type : buildingService.getAttributeTypes()) {
			BuildingAttribute attr = buildings.getAttribute(type);
			if (attr == null) {
				attr = new BuildingAttribute();
				attr.setBuildingAttributeType(type);
			}
			attributeMap.put("" + attr.getBuildingAttributeType().getId(), attr);
		}
	}

	public String execute() throws FlexPayException {
		for (Buildings current : buildingService.getBuildingBuildings(buildings.getBuilding())) {
			if (buildings.getId().longValue() != current.getId().longValue()) {
				alternateBuildingsList.add(buildingService.readFull(stub(current)));
			}
		}

		if (isSubmit()) {
			for (BuildingAttribute attr : attributeMap.values()) {
				buildings.setBuildingAttribute(attr.getValue(), attr.getBuildingAttributeType());
			}

			buildingService.update(buildings);
		}

		return INPUT;
	}

	/**
	 * @param buildingService the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
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
	public Map<String, BuildingAttribute> getAttributeMap() {
		return attributeMap;
	}

	/**
	 * @param attributeMap the attributeMap to set
	 */
	public void setAttributeMap(Map<String, BuildingAttribute> attributeMap) {
		this.attributeMap = attributeMap;
	}
}
