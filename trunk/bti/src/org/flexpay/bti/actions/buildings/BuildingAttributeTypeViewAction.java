package org.flexpay.bti.actions.buildings;

import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.bti.persistence.building.BuildingAttributeTypeEnum;
import org.flexpay.bti.persistence.building.BuildingAttributeTypeEnumValue;
import org.flexpay.bti.persistence.building.BuildingAttributeTypeSimple;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class BuildingAttributeTypeViewAction extends FPActionSupport {

	private BuildingAttributeType attributeType = new BuildingAttributeTypeSimple();
	private Map<Integer, String> enumValues = treeMap();

	private BuildingAttributeTypeService attributeTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (attributeType.isNew()) {
			log.error(getText("error.invalid_id"));
			addActionError(getText("error.invalid_id"));
			return REDIRECT_ERROR;
		}
		attributeType = attributeTypeService.readFull(stub(attributeType));

		if (attributeType == null) {
			log.error(getText("common.object_not_selected"));
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		if (attributeType instanceof BuildingAttributeTypeEnum) {
			BuildingAttributeTypeEnum enumType = (BuildingAttributeTypeEnum) attributeType;
			for (BuildingAttributeTypeEnumValue value : enumType.getValues()) {
				enumValues.put(value.getOrder(), value.getValue());
			}
		}

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
		return REDIRECT_ERROR;
	}

	public BuildingAttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(BuildingAttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public Map<Integer, String> getEnumValues() {
		return enumValues;
	}

	@Required
	public void setAttributeTypeService(BuildingAttributeTypeService attributeTypeService) {
		this.attributeTypeService = attributeTypeService;
	}

}
