package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.NameFilter;
import org.jetbrains.annotations.NotNull;

public class RegionFilter extends NameFilter<Region, RegionNameTranslation> {

	public RegionFilter() {
		setDefaultId(ApplicationConfig.getDefaultRegion().getId());
	}

	public RegionFilter(Long selectedId) {
		super(selectedId);
	}

	public RegionFilter(@NotNull Stub<Region> stub) {
		super(stub);
	}

}
