package org.flexpay.common.persistence.filter;

import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

public class FPModuleFilter extends PrimaryKeyFilter<FPModule> {

	public FPModuleFilter() {
	}

	public FPModuleFilter(Long selectedId) {
		super(selectedId);
	}

	public FPModuleFilter(@NotNull Stub<FPModule> fpModuleStub) {
		super(fpModuleStub);
	}
}
