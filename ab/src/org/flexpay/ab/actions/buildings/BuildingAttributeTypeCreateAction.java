package org.flexpay.ab.actions.buildings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.BuildingAttributeTypeTranslation;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.service.LanguageService;

import com.opensymphony.xwork2.Preparable;

public class BuildingAttributeTypeCreateAction extends FPActionSupport
		implements Preparable {

	private LanguageService languageService;

	private BuildingService buildingService;

	private Map<Long, BuildingAttributeTypeTranslation> translationMap;
	private Integer typeField;

	private String blancTypeFieldError;
	private String typeAlredyExistError;

	public void prepare() {
		List<Language> languages = languageService.getLanguages();
		translationMap = new HashMap<Long, BuildingAttributeTypeTranslation>();
		for (Language language : languages) {
			BuildingAttributeTypeTranslation translation = new BuildingAttributeTypeTranslation();
			translation.setLang(language);
			translationMap.put(language.getId(), translation);
		}
	}

	public String execute() {
		if (isSubmitted()) {
			boolean blancDefaultTranslation = true;
			BuildingAttributeType type = new BuildingAttributeType();
			Set<BuildingAttributeTypeTranslation> translationSet = new HashSet<BuildingAttributeTypeTranslation>();
			for (BuildingAttributeTypeTranslation translation : translationMap.values()) {
				translation.setTranslatable(type);
				translationSet.add(translation);
				if (translation.getLang().isDefault()
						&& !StringUtils.isEmpty(translation.getName())) {
					blancDefaultTranslation = false;
				}
			}

			if (typeField == null) {
				blancTypeFieldError = "ab.buildings.attribute_type.blanc_type_field";
			} else {
				for (BuildingAttributeType attrType : buildingService.getAttributeTypes()) {
					if (typeField == attrType.getType()) {
						typeAlredyExistError = "ab.buildings.attribute_type.type_field_elredy_exist";
						break;
					}
				}
			}

			if (typeAlredyExistError == null && blancTypeFieldError == null
					&& !blancDefaultTranslation) {
				type.setType(typeField);
				type.setTranslations(translationSet);
				buildingService.createBuildingAttributeType(type);

				return "list";
			}
		}

		return "form";
	}

	/**
	 * @return the translationMap
	 */
	public Map<Long, BuildingAttributeTypeTranslation> getTranslationMap() {
		return translationMap;
	}

	/**
	 * @param translationMap
	 *            the translationMap to set
	 */
	public void setTranslationMap(
			Map<Long, BuildingAttributeTypeTranslation> translationMap) {
		this.translationMap = translationMap;
	}

	/**
	 * @return the typeField
	 */
	public Integer getTypeField() {
		return typeField;
	}

	/**
	 * @param typeField
	 *            the typeField to set
	 */
	public void setTypeField(Integer typeField) {
		this.typeField = typeField;
	}

	/**
	 * @param languageService
	 *            the languageService to set
	 */
	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	/**
	 * @param buildingService
	 *            the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	/**
	 * @return the blancTypeFieldError
	 */
	public String getBlancTypeFieldError() {
		return blancTypeFieldError;
	}

	/**
	 * @return the typeAlredyExistError
	 */
	public String getTypeAlredyExistError() {
		return typeAlredyExistError;
	}

	/**
	 * @return the languageService
	 */
	public LanguageService getLanguageService() {
		return languageService;
	}

	/**
	 * @return the buildingService
	 */
	public BuildingService getBuildingService() {
		return buildingService;
	}

}
