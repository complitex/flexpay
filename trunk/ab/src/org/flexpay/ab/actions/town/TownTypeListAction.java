package org.flexpay.ab.actions.town;

import java.util.List;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;

public class TownTypeListAction  extends CommonAction {
	private TownTypeService townTypeService;
	private List<TownTypeTranslation> translationList;

	public String execute() throws Exception {
		translationList = townTypeService.getTranslations(userPreferences.getLocale());

		return "list";
	}

	/**
	 * Setter for property 'townTypeService'.
	 *
	 * @param streetTownService Value to set for property 'townTypeService'.
	 */
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

	public List<TownTypeTranslation> getTranslationList() {
		return translationList;
	}

}
