package org.flexpay.ab.actions.region;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTemporal;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.ab.actions.nametimedependent.SimpleEditAction;

/**
 * Region simple editor
 */
public class RegionEditSimple extends SimpleEditAction<
		RegionName, RegionNameTemporal, Region, RegionNameTranslation> {

	public RegionEditSimple() {
		setObject(new Region());
	}

}
