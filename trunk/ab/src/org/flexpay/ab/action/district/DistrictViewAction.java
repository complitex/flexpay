package org.flexpay.ab.action.district;

import org.flexpay.ab.action.ObjectViewAction;
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
