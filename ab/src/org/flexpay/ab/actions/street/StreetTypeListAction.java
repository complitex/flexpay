package org.flexpay.ab.actions.street;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.service.StreetTypeService;

import java.util.List;

public class StreetTypeListAction extends CommonAction {

	private StreetTypeService streetTypeService;
	private List<StreetTypeTranslation> translationList;

	public String execute() throws Exception {
		translationList = streetTypeService.getTranslations(userPreferences.getLocale());

		return "list";
	}

	/**
	 * Setter for property 'streetTypeService'.
	 *
	 * @param streetTypeService Value to set for property 'streetTypeService'.
	 */
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	public List<StreetTypeTranslation> getTranslationList() {
		return translationList;
	}
}
