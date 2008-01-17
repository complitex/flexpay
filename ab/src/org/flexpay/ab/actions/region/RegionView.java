package org.flexpay.ab.actions.region;

import org.flexpay.ab.actions.nametimedependent.ObjectViewAction;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTemporal;
import org.flexpay.ab.persistence.RegionNameTranslation;

public class RegionView extends ObjectViewAction<
		RegionName, RegionNameTemporal, Region, RegionNameTranslation> {

	public RegionView() {
		setObject(new Region());
	}
}
