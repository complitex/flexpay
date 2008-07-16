package org.flexpay.ab.actions.buildings;

import java.util.List;

import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;

public class BuildingAttributeTypeListAction extends FPActionSupport {
	
	private BuildingService buildingService;
	
	private List<BuildingAttributeType> types;
	
	public String execute() {
		types = buildingService.getAttributeTypes();
		
		return "list";
	}

	/**
	 * @param buildingService the biuldingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	/**
	 * @return the types
	 */
	public List<BuildingAttributeType> getTypes() {
		return types;
	}

}
