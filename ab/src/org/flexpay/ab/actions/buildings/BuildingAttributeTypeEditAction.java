package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.BuildingAttributeTypeTranslation;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.BuildingAttributeTypeService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;


public class BuildingAttributeTypeEditAction extends FPActionSupport {

	private BuildingAttributeTypeService buildingAttributeTypeService;

	private BuildingAttributeType attributeType = new BuildingAttributeType();
	private Map<Long, String> names = treeMap();

	@NotNull
	public String doExecute() throws Exception {

		if (attributeType.getId() == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		BuildingAttributeType type = attributeType.isNew() ?
									 attributeType :
									 buildingAttributeTypeService.read(stub(attributeType));

		if (!isSubmit()) {
			attributeType = type;
			initTranslations();
			return INPUT;
		}

		// init translations
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			BuildingAttributeTypeTranslation translation = new BuildingAttributeTypeTranslation();
			translation.setLang(lang);
			translation.setName(value);
			type.setTranslation(translation);
		}

		buildingAttributeTypeService.save(type);

		return REDIRECT_SUCCESS;
	}

	private void initTranslations() {

		for (BuildingAttributeTypeTranslation translation : attributeType.getTranslations()) {
			names.put(translation.getLang().getId(), translation.getName());
		}

		for (Language language : ApplicationConfig.getLanguages()) {
			if (!names.containsKey(language.getId())) {
				names.put(language.getId(), "");
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

	@Required
	public void setBuildingAttributeTypeService(BuildingAttributeTypeService buildingAttributeTypeService) {
		this.buildingAttributeTypeService = buildingAttributeTypeService;
	}
}
