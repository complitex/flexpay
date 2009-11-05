package org.flexpay.ab.actions.town;

import org.flexpay.ab.actions.ObjectViewAction;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.persistence.Stub;
import org.springframework.beans.factory.annotation.Required;

public class TownViewAction extends ObjectViewAction<
		TownName, TownNameTemporal, Town, TownNameTranslation> {

	private TownTypeService townTypeService;

	public TownViewAction() {
		setObject(new Town());
	}

	public TownType type(Long id) {
		return townTypeService.readFull(new Stub<TownType>(id));
	}

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

}
