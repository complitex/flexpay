package org.flexpay.ab.actions.town;

import org.flexpay.ab.actions.nametimedependent.ObjectViewAction;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.persistence.TownNameTemporal;
import org.flexpay.ab.persistence.TownNameTranslation;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;

import java.util.Collection;

public class TownView extends ObjectViewAction<
		TownName, TownNameTemporal, Town, TownNameTranslation> {

	public TownView() {
		setObject(new Town());
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
