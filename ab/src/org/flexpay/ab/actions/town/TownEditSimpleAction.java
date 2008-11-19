package org.flexpay.ab.actions.town;

import org.flexpay.ab.actions.nametimedependent.SimpleEditAction;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.persistence.TownNameTemporal;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownNameTranslation;

public class TownEditSimpleAction extends SimpleEditAction<
		TownName, TownNameTemporal, Town, TownNameTranslation> {

	public TownEditSimpleAction() {
		setObject(new Town());
	}
}
