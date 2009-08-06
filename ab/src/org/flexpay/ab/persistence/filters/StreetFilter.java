package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetNameTranslation;
import org.flexpay.common.persistence.filter.NameFilter;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

public class StreetFilter extends NameFilter<Street, StreetNameTranslation> {

	public StreetFilter() {
	}

	public StreetFilter(Long selectedId) {
		super(selectedId);
	}

	public StreetFilter(@NotNull Stub<Street> streetStub) {
		super(streetStub);
	}
}
