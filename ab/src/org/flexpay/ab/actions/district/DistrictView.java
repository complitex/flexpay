package org.flexpay.ab.actions.district;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.actions.nametimedependent.ObjectViewAction;

public class DistrictView extends ObjectViewAction<
		DistrictName, DistrictNameTemporal, District, DistrictNameTranslation> {

	public DistrictView() {
		setObject(new District());
	}
}
