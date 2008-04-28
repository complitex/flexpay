package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.TownNameTranslation;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.filter.NameFilter;

public class TownFilter extends NameFilter<TownNameTranslation> {

	public TownFilter() {
		setDefaultId(ApplicationConfig.getInstance().getDefaultTown().getId());
	}

	public TownFilter(Long selectedId) {
		super(selectedId);
	}
}
