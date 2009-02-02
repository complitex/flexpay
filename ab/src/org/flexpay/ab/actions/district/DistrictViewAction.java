package org.flexpay.ab.actions.district;

import org.flexpay.ab.actions.nametimedependent.ObjectViewAction;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.DistrictNameTemporal;
import org.flexpay.ab.persistence.DistrictNameTranslation;

public class DistrictViewAction extends ObjectViewAction<
		DistrictName, DistrictNameTemporal, District, DistrictNameTranslation> {

	public DistrictViewAction() {
		setObject(new District());
	}
}
