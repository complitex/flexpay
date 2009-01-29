package org.flexpay.tc.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.Date;
import java.util.List;

public interface TariffCalculationResultService {

	@Secured({Roles.TARIFF_CALCULATION_RESULT_ADD})
	void add(@NotNull TariffCalculationResult tariffCalculationResult);

	@Secured(Roles.TARIFF_CALCULATION_RESULT_READ)
	TariffCalculationResult read(@NotNull Stub<TariffCalculationResult> stub);

	@Secured(Roles.TARIFF_CALCULATION_RESULT_READ)
	List<TariffCalculationResult> getTariffCalcResultsByCalcDateAndBuildingId(@NotNull Date calcDate, @NotNull Long buildingId);

	@Secured(Roles.TARIFF_CALCULATION_RESULT_READ)
	List<Date> getUniqueDates();

}
