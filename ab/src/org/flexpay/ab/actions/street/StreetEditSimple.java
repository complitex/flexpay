package org.flexpay.ab.actions.street;

import org.flexpay.ab.actions.nametimedependent.SimpleEditAction;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetName;
import org.flexpay.ab.persistence.StreetNameTemporal;
import org.flexpay.ab.persistence.StreetNameTranslation;

public class StreetEditSimple extends SimpleEditAction<
		StreetName, StreetNameTemporal, Street, StreetNameTranslation> {

	public StreetEditSimple() {
		setObject(new Street());
	}
}