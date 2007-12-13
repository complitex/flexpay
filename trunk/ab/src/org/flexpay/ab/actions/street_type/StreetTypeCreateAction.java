package org.flexpay.ab.actions.street_type;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.service.MultilangEntityService;
import org.flexpay.ab.service.StreetTypeService;

public class StreetTypeCreateAction extends
		AbstractCreateAction<StreetType, StreetTypeTranslation> {

	private StreetTypeService streetTypeService;

	protected StreetTypeTranslation createTranslation() {
		return new StreetTypeTranslation();
	}

	protected MultilangEntityService<StreetType, StreetTypeTranslation> getEntityService() {
		return streetTypeService;
	}

	/**
	 * Setter for property 'streetTypeService'.
	 * 
	 * @param streetTypeService
	 *            Value to set for property 'streetTypeService'.
	 */
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}
}
