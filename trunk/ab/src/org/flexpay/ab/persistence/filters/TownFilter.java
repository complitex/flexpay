package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.TownNameTranslation;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.filter.NameFilter;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

public class TownFilter extends NameFilter<Town, TownNameTranslation> {

	public TownFilter() {
		setDefaultId(ApplicationConfig.getDefaultTown().getId());
	}

	public TownFilter(Long selectedId) {
		super(selectedId);
	}

	public TownFilter(@NotNull Stub<Town> stub) {
		super(stub);
	}
}
