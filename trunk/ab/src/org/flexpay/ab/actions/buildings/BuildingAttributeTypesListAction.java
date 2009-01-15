package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.service.BuildingAttributeTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class BuildingAttributeTypesListAction extends FPActionSupport {

	private BuildingAttributeTypeService buildingAttributeTypeService;

	private List<BuildingAttributeType> types;

	@NotNull
	public String doExecute() {
		types = buildingAttributeTypeService.getAttributeTypes();

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
	 * @return the types
	 */
	public List<BuildingAttributeType> getTypes() {
		return types;
	}

	@Required
	public void setBuildingAttributeTypeService(BuildingAttributeTypeService buildingAttributeTypeService) {
		this.buildingAttributeTypeService = buildingAttributeTypeService;
	}
}
