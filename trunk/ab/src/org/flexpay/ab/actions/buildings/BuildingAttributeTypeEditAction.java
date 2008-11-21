package org.flexpay.ab.actions.buildings;

import com.opensymphony.xwork2.Preparable;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.BuildingAttributeTypeTranslation;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.Translation;
import static org.flexpay.common.util.CollectionUtils.map;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;


public class BuildingAttributeTypeEditAction extends FPActionSupport implements Preparable {

	private BuildingService buildingService;

	private BuildingAttributeType buildingAttributeType = new BuildingAttributeType();
	private Map<Long, BuildingAttributeTypeTranslation> translationMap = map();

	public void prepare() {
		List<Language> languages = ApplicationConfig.getLanguages();
		for (Language language : languages) {
			BuildingAttributeTypeTranslation translation = new BuildingAttributeTypeTranslation();
			translation.setLang(language);
			translationMap.put(language.getId(), translation);
		}
	}

	@NotNull
	public String doExecute() throws FlexPayException {
		buildingAttributeType = buildingService.getAttributeType(stub(buildingAttributeType));

		if (isSubmit()) {
			for (BuildingAttributeTypeTranslation translation : translationMap.values()) {
				BuildingAttributeTypeTranslation persistentTranslation = getTranslationByLang(buildingAttributeType, translation.getLang());
				if (translation.isBlank()) {
					if (persistentTranslation != null) {
						buildingAttributeType.getTranslations().remove(persistentTranslation);
					}
				} else {
					if (persistentTranslation != null) {
						persistentTranslation.setName(translation.getName());
						persistentTranslation.setShortName(translation.getShortName());
					} else {
						translation.setTranslatable(buildingAttributeType);
						buildingAttributeType.getTranslations().add(translation);
					}
				}
			}

			if (buildingAttributeType.getTranslations().isEmpty()) {
				// hz che hotel sdelat
			} else {
				buildingService.updateBuildingAttributeType(buildingAttributeType);
				return REDIRECT_SUCCESS;
			}
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			boolean exist = false;
			for (Translation t : buildingAttributeType.getTranslations()) {
				if (t.getLang().equals(lang)) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				BuildingAttributeTypeTranslation t = new BuildingAttributeTypeTranslation();
				t.setLang(lang);
				t.setTranslatable(buildingAttributeType);
				buildingAttributeType.getTranslations().add(t);
			}
		}

		return INPUT;
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
		return REDIRECT_SUCCESS;
	}

	private BuildingAttributeTypeTranslation getTranslationByLang(BuildingAttributeType attrType, Language lang) {
		for (BuildingAttributeTypeTranslation t : attrType.getTranslations()) {
			if (t.getLang().equals(lang)) {
				return t;
			}
		}

		return null;
	}

	/**
	 * @return the buildingAttributeType
	 */
	public BuildingAttributeType getBuildingAttributeType() {
		return buildingAttributeType;
	}

	/**
	 * @param buildingAttributeType the buildingAttributeType to set
	 */
	public void setBuildingAttributeType(BuildingAttributeType buildingAttributeType) {
		this.buildingAttributeType = buildingAttributeType;
	}

	/**
	 * @return the translationMap
	 */
	public Map<Long, BuildingAttributeTypeTranslation> getTranslationMap() {
		return translationMap;
	}

	/**
	 * @param translationMap the translationMap to set
	 */
	public void setTranslationMap(Map<Long, BuildingAttributeTypeTranslation> translationMap) {
		this.translationMap = translationMap;
	}

	/**
	 * @param buildingService the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}
