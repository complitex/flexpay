package org.flexpay.bti.action.building;

import org.flexpay.bti.persistence.building.*;
import org.flexpay.bti.persistence.filters.BuildingAttributeGroupFilter;
import org.flexpay.bti.service.BuildingAttributeGroupService;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

public class BuildingAttributeTypeEditAction extends FPActionSupport {

	private static final String TYPE_SIMPLE = "simple";
	private static final String TYPE_ENUM = "enum";

	private BuildingAttributeType attributeType = new BuildingAttributeTypeSimple();
	private BuildingAttributeGroupFilter buildingAttributeGroupFilter = new BuildingAttributeGroupFilter();
	private Map<Long, String> names = treeMap();
	private Map<Integer, String> enumValues = treeMap();

	private String typeName = TYPE_SIMPLE;
	private int temporal;

	private String crumbCreateKey;
	private BuildingAttributeTypeService attributeTypeService;
	private BuildingAttributeGroupService attributeGroupService;

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

		attributeType = attributeType.isNew() ? attributeType : attributeTypeService.readFull(stub(attributeType));

		if (attributeType == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		attributeGroupService.initFilter(buildingAttributeGroupFilter);

		if (isNotSubmit()) {
			if (attributeType instanceof BuildingAttributeTypeSimple) {
				typeName = TYPE_SIMPLE;
			} else if (attributeType instanceof BuildingAttributeTypeEnum) {
				typeName = TYPE_ENUM;
			}
			if (attributeType.isNotNew()) {
				buildingAttributeGroupFilter.setSelectedId(attributeType.getGroup().getId());
			}
			temporal = attributeType.isTemp() ? 1 : 0;
			initEnumValues();
			initTranslations();
			return INPUT;
		}

		// for a new objects can specify type
		if (attributeType.isNew()) {
			if (TYPE_SIMPLE.equals(typeName)) {
				attributeType = new BuildingAttributeTypeSimple(0L);
			} else if (TYPE_ENUM.equals(typeName)){
				attributeType = new BuildingAttributeTypeEnum(0L);
			}
		}

		if (buildingAttributeGroupFilter.needFilter()) {
			attributeType.setGroup(new BuildingAttributeGroup(buildingAttributeGroupFilter.getSelectedStub()));
		}

		// init translations
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			attributeType.setTranslation(new BuildingAttributeTypeName(value, lang));
		}

		if (attributeType instanceof BuildingAttributeTypeEnum) {
			BuildingAttributeTypeEnum typeEnum = (BuildingAttributeTypeEnum) attributeType;
			log.debug("Before setting values: {}", typeEnum.getValues());
			typeEnum.rawValues(enumValues);
			log.debug("After setting values: {}", typeEnum.getValues());
		}

		attributeType.setTemp(temporal != 0);

		if (attributeType.isNew()) {
			attributeTypeService.create(attributeType);
		} else {
			attributeTypeService.update(attributeType);
		}

		addActionMessage(getText("bti.building.attribute.type.saved"));

		return REDIRECT_SUCCESS;
	}

	private void initTranslations() {

		for (BuildingAttributeTypeName name : attributeType.getTranslations()) {
			names.put(name.getLang().getId(), name.getName());
		}

		for (Language lang : getLanguages()) {
			if (!names.containsKey(lang.getId())) {
				names.put(lang.getId(), "");
			}
		}
	}

	private void initEnumValues() {
    	if (attributeType instanceof BuildingAttributeTypeEnum) {
			BuildingAttributeTypeEnum enumType = (BuildingAttributeTypeEnum) attributeType;
			for (BuildingAttributeTypeEnumValue value : enumType.getValues()) {
				enumValues.put(value.getOrder(), value.getValue());
			}
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

	@Override
	protected void setBreadCrumbs() {
		if (attributeType.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public BuildingAttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(BuildingAttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Map<Integer, String> getEnumValues() {
		return enumValues;
	}

	public void setEnumValues(Map<Integer, String> enumValues) {
		this.enumValues = enumValues;
	}

	public BuildingAttributeGroupFilter getBuildingAttributeGroupFilter() {
		return buildingAttributeGroupFilter;
	}

	public void setBuildingAttributeGroupFilter(BuildingAttributeGroupFilter buildingAttributeGroupFilter) {
		this.buildingAttributeGroupFilter = buildingAttributeGroupFilter;
	}

	public int getTemporal() {
		return temporal;
	}

	public void setTemporal(int temporal) {
		this.temporal = temporal;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setAttributeTypeService(BuildingAttributeTypeService attributeTypeService) {
		this.attributeTypeService = attributeTypeService;
	}

	@Required
	public void setAttributeGroupService(BuildingAttributeGroupService attributeGroupService) {
		this.attributeGroupService = attributeGroupService;
	}

}
