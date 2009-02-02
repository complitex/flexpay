package org.flexpay.ab.actions.region;

import org.flexpay.ab.actions.nametimedependent.SimpleEditAction;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTemporal;
import org.flexpay.ab.persistence.RegionNameTranslation;

/**
 * Region simple editor
 */
public class RegionEditSimpleAction extends SimpleEditAction<
		RegionName, RegionNameTemporal, Region, RegionNameTranslation> {

	public RegionEditSimpleAction() {
		setObject(new Region());
	}
}
