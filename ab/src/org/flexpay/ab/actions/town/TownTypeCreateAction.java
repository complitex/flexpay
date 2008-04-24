package org.flexpay.ab.actions.town;

import org.flexpay.ab.actions.AbstractCreateAction;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.MultilangEntityService;
import org.flexpay.ab.service.TownTypeService;

public class TownTypeCreateAction extends
		AbstractCreateAction<TownType, TownTypeTranslation> {
	private TownTypeService townTypeService;

	protected TownTypeTranslation createTranslation() {
		return new TownTypeTranslation();
	}

	protected MultilangEntityService<TownType, TownTypeTranslation> getEntityService() {
		return townTypeService;
	}

	/**
	 * Setter for property 'townTypeService'.
	 * 
	 * @param townTypeService
	 *            Value to set for property 'townTypeService'.
	 */
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

}
