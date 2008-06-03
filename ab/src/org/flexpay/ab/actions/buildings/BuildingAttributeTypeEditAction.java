package org.flexpay.ab.actions.buildings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.BuildingAttributeTypeTranslation;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.service.LanguageService;

import com.opensymphony.xwork2.Preparable;


public class BuildingAttributeTypeEditAction extends FPActionSupport implements Preparable {
	
	private LanguageService languageService;

	private BuildingService buildingService;
	
	private Integer type;
	private BuildingAttributeType buildingAttributeType;
	
	private Map<Long, BuildingAttributeTypeTranslation> translationMap;
	
	private String allTranslationBlancError;
	
	public void prepare() {
		List<Language> languages = languageService.getLanguages();
		translationMap = new HashMap<Long, BuildingAttributeTypeTranslation>();
		for (Language language : languages) {
			BuildingAttributeTypeTranslation translation = new BuildingAttributeTypeTranslation();
			translation.setLang(language);
			translationMap.put(language.getId(), translation);
		}
	}
	

	public String execute() throws FlexPayException {
		buildingAttributeType = buildingService.getAttributeType(type);
		
		if(isSubmitted()) {
			for(BuildingAttributeTypeTranslation translation : translationMap.values()) {
				BuildingAttributeTypeTranslation persistentTranslation = getTranslationByLang(buildingAttributeType, translation.getLang());
				if(isBlancTranslation(translation)) {
					if(persistentTranslation != null) {
					    buildingAttributeType.getTranslations().remove(persistentTranslation);
					}
				} else {
					if(persistentTranslation != null) {
						persistentTranslation.setName(translation.getName());
						persistentTranslation.setShortName(translation.getShortName());
					} else {
						translation.setTranslatable(buildingAttributeType);
						buildingAttributeType.getTranslations().add(translation);
					}
				}
			}
			
			if(buildingAttributeType.getTranslations().isEmpty()) {
				allTranslationBlancError = "";
			} else {
			    buildingService.updateBuildingAttributeType(buildingAttributeType);
			    return "list";
			}
		}
		
		
		for(Language lang : languageService.getLanguages()) {
			boolean exist = false;
			for(Translation t : buildingAttributeType.getTranslations()) {
				if(t.getLang().equals(lang)) {
				    exist = true;	
					break;
				}
			}
			if(!exist) {
				BuildingAttributeTypeTranslation t = new BuildingAttributeTypeTranslation();
				t.setLang(lang);
				t.setTranslatable(buildingAttributeType);
				buildingAttributeType.getTranslations().add(t);
			}
		}
		
		
		
		return "form";
	}
	
	private BuildingAttributeTypeTranslation getTranslationByLang(BuildingAttributeType attrType, Language lang) {
		for(BuildingAttributeTypeTranslation t : attrType.getTranslations()) {
			if(t.getLang().equals(lang)) {
				return t;
			}
		}
		
		return null;
	}
	
	private boolean isBlancTranslation(BuildingAttributeTypeTranslation t) {
		if(StringUtils.isEmpty(t.getName()) && StringUtils.isEmpty(t.getShortName())) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * @return the languageService
	 */
	public LanguageService getLanguageService() {
		return languageService;
	}


	/**
	 * @param languageService the languageService to set
	 */
	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}


	/**
	 * @return the buildingService
	 */
	public BuildingService getBuildingService() {
		return buildingService;
	}


	/**
	 * @param buildingService the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}


	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
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
	 * @return the allTranslationBlancError
	 */
	public String getAllTranslationBlancError() {
		return allTranslationBlancError;
	}


	/**
	 * @param allTranslationBlancError the allTranslationBlancError to set
	 */
	public void setAllTranslationBlancError(String allTranslationBlancError) {
		this.allTranslationBlancError = allTranslationBlancError;
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
	

}
