package org.flexpay.ab.action.region;

import org.flexpay.ab.action.ObjectViewAction;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTemporal;
import org.flexpay.ab.persistence.RegionNameTranslation;

public class RegionViewAction extends ObjectViewAction<
		RegionName, RegionNameTemporal, Region, RegionNameTranslation> {

	public RegionViewAction() {
		setObject(new Region());
	}

}
