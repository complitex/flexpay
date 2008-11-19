package org.flexpay.ab.actions.street;

import org.flexpay.ab.actions.nametimedependent.ObjectViewAction;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetName;
import org.flexpay.ab.persistence.StreetNameTemporal;
import org.flexpay.ab.persistence.StreetNameTranslation;

public class StreetViewAction extends ObjectViewAction<
		StreetName, StreetNameTemporal, Street, StreetNameTranslation> {

	public StreetViewAction() {
		setObject(new Street());
	}
}