package org.flexpay.ab.actions.street;

import org.flexpay.ab.actions.nametimedependent.SimpleEditAction;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetName;
import org.flexpay.ab.persistence.StreetNameTemporal;
import org.flexpay.ab.persistence.StreetNameTranslation;

public class StreetEditSimpleAction extends SimpleEditAction<
		StreetName, StreetNameTemporal, Street, StreetNameTranslation> {

	public StreetEditSimpleAction() {
		setObject(new Street());
	}
}