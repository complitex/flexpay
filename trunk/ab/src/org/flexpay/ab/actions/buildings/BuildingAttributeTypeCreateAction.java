package org.flexpay.ab.actions.buildings;

import com.opensymphony.xwork2.Preparable;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.BuildingAttributeTypeTranslation;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BuildingAttributeTypeCreateAction extends FPActionSupport implements Preparable {

	private BuildingService buildingService;

	private Map<Long, BuildingAttributeTypeTranslation> translationMap;

	public void prepare() {
		List<Language> languages = ApplicationConfig.getLanguages();
		translationMap = map();
		for (Language language : languages) {
			BuildingAttributeTypeTranslation translation = new BuildingAttributeTypeTranslation();
			translation.setLang(language);
			translationMap.put(language.getId(), translation);
		}
	}

	@NotNull
	public String doExecute() {

		if (isSubmit()) {
			boolean blancDefaultTranslation = true;
			BuildingAttributeType type = new BuildingAttributeType();
			Set<BuildingAttributeTypeTranslation> translationSet = set();
			for (BuildingAttributeTypeTranslation translation : translationMap.values()) {
				translation.setTranslatable(type);
				translationSet.add(translation);
				if (translation.getLang().isDefault()
					&& !StringUtils.isEmpty(translation.getName())) {
					blancDefaultTranslation = false;
				}
			}

			if (!blancDefaultTranslation) {
				type.setTranslations(translationSet);
				buildingService.createBuildingAttributeType(type);

				return REDIRECT_SUCCESS;
			}

			addActionError(getText("common.error.no_default_lang_name"));
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
	@Override
	protected String getErrorResult() {
		return INPUT;
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
	public void setTranslationMap(
			Map<Long, BuildingAttributeTypeTranslation> translationMap) {
		this.translationMap = translationMap;
	}

	/**
	 * @param buildingService the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}
