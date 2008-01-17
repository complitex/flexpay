package org.flexpay.ab.actions.town;

import org.flexpay.ab.actions.nametimedependent.ObjectViewAction;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.persistence.TownNameTemporal;
import org.flexpay.ab.persistence.TownNameTranslation;

public class TownView extends ObjectViewAction<
		TownName, TownNameTemporal, Town, TownNameTranslation> {

	public TownView() {
		setObject(new Town());
	}
}
