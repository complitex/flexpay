package org.flexpay.tc.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.persistence.TariffCalcRegulation;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.Set;

public interface TariffCalcRegulationService {

	@Secured({Roles.TARIFF_CALCULATION_REGULATION_ADD, Roles.TARIFF_CALCULATION_REGULATION_CHANGE})
	void save(@NotNull TariffCalcRegulation tariffCalcRegulation);

	@Secured(Roles.TARIFF_CALCULATION_REGULATION_READ)
	TariffCalcRegulation read(@NotNull Stub<TariffCalcRegulation> stub);

	@Secured({Roles.TARIFF_CALCULATION_REGULATION_DELETE})
	void disable(@NotNull Set<Long> objectIds);

}
