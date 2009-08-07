package org.flexpay.bti.actions.buildings;

import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.bti.persistence.building.*;
import org.flexpay.bti.persistence.filters.BuildingAttributeGroupFilter;
import org.flexpay.bti.service.BuildingAttributeGroupService;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class BuildingAttributeTypeEditAction extends FPActionSupport {

	private static final String TYPE_SIMPLE = "simple";
	private static final String TYPE_ENUM = "enum";

	private BuildingAttributeType attributeType = new BuildingAttributeTypeSimple();
	private BuildingAttributeGroupFilter buildingAttributeGroupFilter = new BuildingAttributeGroupFilter();
	private Map<Long, String> names = treeMap();
	private Map<Integer, String> enumValues = treeMap();

	private String typeName = TYPE_SIMPLE;
	private int temporal;

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
	protected String doExecute() throws Exception {

		if (attributeType.getId() == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		BuildingAttributeType type = attributeType.isNew() ?
									 attributeType : attributeTypeService.readFull(stub(attributeType));
		if (type == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		attributeGroupService.initFilter(buildingAttributeGroupFilter);

		if (!isSubmit()) {
			attributeType = type;
			if (type instanceof BuildingAttributeTypeSimple) {
				typeName = TYPE_SIMPLE;
			} else if (type instanceof BuildingAttributeTypeEnum) {
				typeName = TYPE_ENUM;
			}
			if (type.isNotNew()) {
				buildingAttributeGroupFilter.setSelectedId(type.getGroup().getId());
			}
			temporal = attributeType.isTemp() ? 1 : 0;
			initEnumValues();
			initTranslations();
			return INPUT;
		}

		// for a new objects can specify type
		if (type.isNew()) {
			if (TYPE_SIMPLE.equals(typeName)) {
				type = new BuildingAttributeTypeSimple(0L);
			} else if (TYPE_ENUM.equals(typeName)){
				type = new BuildingAttributeTypeEnum(0L);
			}
		}

		if (buildingAttributeGroupFilter.needFilter()) {
			type.setGroup(new BuildingAttributeGroup(buildingAttributeGroupFilter.getSelectedStub()));
		}

		// init translations
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			BuildingAttributeTypeName translation = new BuildingAttributeTypeName();
			translation.setLang(lang);
			translation.setName(value);
			type.setTranslation(translation);
		}

		if (type instanceof BuildingAttributeTypeEnum) {
			BuildingAttributeTypeEnum typeEnum = (BuildingAttributeTypeEnum) type;
			log.debug("Before setting values: {}", typeEnum.getValues());
			typeEnum.rawValues(enumValues);
			log.debug("After setting values: {}", typeEnum.getValues());
		}

		type.setTemp(temporal != 0);

		if (type.isNew()) {
			attributeTypeService.create(type);
		} else {
			attributeTypeService.update(type);
		}

		addActionError(getText("bti.building.attribute.type.saved"));
		return REDIRECT_SUCCESS;
	}

	private void initTranslations() {

		for (BuildingAttributeTypeName translation : attributeType.getTranslations()) {
			names.put(translation.getLang().getId(), translation.getName());
		}

		for (Language language : ApplicationConfig.getLanguages()) {
			if (!names.containsKey(language.getId())) {
				names.put(language.getId(), "");
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
	protected String getErrorResult() {
		return INPUT;
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

	@Required
	public void setAttributeTypeService(BuildingAttributeTypeService attributeTypeService) {
		this.attributeTypeService = attributeTypeService;
	}

	@Required
	public void setAttributeGroupService(BuildingAttributeGroupService attributeGroupService) {
		this.attributeGroupService = attributeGroupService;
	}

}
