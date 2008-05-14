package org.flexpay.ab.actions.buildings;

import java.util.ArrayList;
import java.util.List;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.BuildingAttribute;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;

public class BuildingsEditAction extends CommonAction {

	private Buildings buildings;
	private BuildingService buildingService;
	private BuildingAttributeType typeNumber;
	private BuildingAttributeType typeBulk;
	
	private String numberVal;
	private String bulkVal;

	public String execute() throws FlexPayException {
		typeNumber = buildingService.getAttributeType(BuildingAttributeType.TYPE_NUMBER);
		typeBulk = buildingService.getAttributeType(BuildingAttributeType.TYPE_BULK);
		buildings = buildingService.readFull(buildings.getId());
		if(isSubmitted()) {
			buildings.setBuildingAttribute(numberVal, typeNumber);
			buildings.setBuildingAttribute(bulkVal, typeBulk);
			buildingService.update(buildings);
		}

		return "form";
	}

	/**
	 * @param buildingService
	 *            the buildingService to set
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
	 * @param buildings
	 *            the buildings to set
	 */
	public void setBuildings(Buildings buildings) {
		this.buildings = buildings;
	}

	/**
	 * @return the typeNumber
	 */
	public BuildingAttributeType getTypeNumber() {
		return typeNumber;
	}

	/**
	 * @return the typeBulk
	 */
	public BuildingAttributeType getTypeBulk() {
		return typeBulk;
	}

	/**
	 * @param numberVal the numberVal to set
	 */
	public void setNumberVal(String numberVal) {
		this.numberVal = numberVal;
	}

	/**
	 * @param bulkVal the bulkVal to set
	 */
	public void setBulkVal(String bulkVal) {
		this.bulkVal = bulkVal;
	}


}
