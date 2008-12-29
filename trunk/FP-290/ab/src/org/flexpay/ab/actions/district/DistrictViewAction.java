package org.flexpay.ab.actions.district;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.actions.nametimedependent.ObjectViewAction;

public class DistrictViewAction extends ObjectViewAction<
		DistrictName, DistrictNameTemporal, District, DistrictNameTranslation> {

	public DistrictViewAction() {
		setObject(new District());
	}
}
