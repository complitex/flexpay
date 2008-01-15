package org.flexpay.ab.actions.region;

import org.flexpay.ab.actions.nametimedependent.ObjectViewAction;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTemporal;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;

import java.util.Collection;

public class RegionView extends ObjectViewAction<
		RegionName, RegionNameTemporal, Region, RegionNameTranslation> {

	public RegionView() {
		setObject(new Region());
	}

	/**
	 * Get initial set of filters for action
	 *
	 * @return Collection of filters
	 */
	protected Collection<PrimaryKeyFilter> getFilters() {
		return null;
	}

	/**
	 * Set filters for action
	 *
	 * @param filters collection of filters
	 */
	protected void setFilters(Collection<PrimaryKeyFilter> filters) {
	}
}
