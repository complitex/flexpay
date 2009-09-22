package org.flexpay.bti.actions.buildings;

import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class BuildingAttributeTypesListAction extends FPActionWithPagerSupport<BuildingAttributeType> {

	private List<BuildingAttributeType> types = list();

	private BuildingAttributeTypeService attributeTypeService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	@Override
	protected String doExecute() throws Exception {

		log.debug("1 Before: pager = {}", getPager());

		types = attributeTypeService.listTypes(getPager());

		log.debug("1 After pager = {}", getPager());

		return SUCCESS;
	}

	public String getName(Long typeId) {
		BuildingAttributeType type = attributeTypeService.readFull(new Stub<BuildingAttributeType>(typeId));
		if (type == null) {
			log.info("No type found with id {}", typeId);
			return ERROR;
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
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public List<BuildingAttributeType> getTypes() {
		return types;
	}

	@Required
	public void setAttributeTypeService(BuildingAttributeTypeService attributeTypeService) {
		this.attributeTypeService = attributeTypeService;
	}

}
