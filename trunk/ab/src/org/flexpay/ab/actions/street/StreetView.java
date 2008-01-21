package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.actions.nametimedependent.ObjectViewAction;

public class StreetView extends ObjectViewAction<
		StreetName, StreetNameTemporal, Street, StreetNameTranslation> {

	public StreetView() {
		setObject(new Street());
	}
}