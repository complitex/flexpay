package org.flexpay.ab.actions.district;

import org.flexpay.ab.actions.nametimedependent.SimpleEditAction;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.DistrictNameTemporal;
import org.flexpay.ab.persistence.DistrictNameTranslation;

public class DistrictEditSimple extends SimpleEditAction<
		DistrictName, DistrictNameTemporal, District, DistrictNameTranslation> {

	public DistrictEditSimple() {
		setObject(new District());
	}
}
