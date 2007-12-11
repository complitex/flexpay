package org.flexpay.ab.actions;

import java.util.List;

import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.actions.interceptor.UserPreferencesAware;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.config.UserPreferences;

public class StreetTypesListAction implements UserPreferencesAware {
	private StreetTypeService streetTypeService;
	private UserPreferences userPreferences;
	private List<StreetTypeTranslation> translations;

	public String execute() {
		try {
			this.translations = streetTypeService
					.getStreetTypeTranslations(userPreferences.getLocale());
		} catch (FlexPayException e) {
			// TODO
		}

		return "success";
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	public List<StreetTypeTranslation> getTranslations() {
		return translations;
	}
}
