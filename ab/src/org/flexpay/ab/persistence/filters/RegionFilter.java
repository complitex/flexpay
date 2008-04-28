package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.filter.NameFilter;

public class RegionFilter extends NameFilter<RegionNameTranslation> {
	public RegionFilter() {
		setDefaultId(ApplicationConfig.getInstance().getDefaultRegion().getId());
	}
}
