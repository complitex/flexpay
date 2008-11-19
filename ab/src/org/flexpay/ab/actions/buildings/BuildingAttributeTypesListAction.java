package org.flexpay.ab.actions.buildings;

import java.util.List;

import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class BuildingAttributeTypesListAction extends FPActionSupport {
	
	private BuildingService buildingService;
	
	private List<BuildingAttributeType> types;
	
	@NotNull
	public String doExecute() {
		types = buildingService.getAttributeTypes();
		
		return SUCCESS;
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
		return SUCCESS;
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
