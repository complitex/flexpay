package org.flexpay.tc.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.ab.persistence.Building;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

public interface TariffCalculationResultService {

	/**
	 * Save tariff calculation results for building
	 * @param tariffCalculationResult calculated result
	 */	
	@Secured({Roles.TARIFF_CALCULATION_RESULT_ADD})
	void add(@NotNull TariffCalculationResult tariffCalculationResult);

	/**
	 * Save tariff calculation results for building
	 * @param value calculated value
	 * @param creationDate record creation date
	 * @param calculationDate record calculation date
	 * @param building building
	 * @param tariff tariff
	 */
	@Secured({Roles.TARIFF_CALCULATION_RESULT_ADD})
	void add(@NotNull BigDecimal value, Date creationDate, Date calculationDate, @NotNull Building building, @NotNull Tariff tariff);

	@Secured(Roles.TARIFF_CALCULATION_RESULT_READ)
	TariffCalculationResult read(@NotNull Stub<TariffCalculationResult> stub);

	@Secured(Roles.TARIFF_CALCULATION_RESULT_READ)
	List<TariffCalculationResult> getTariffCalcResultsByCalcDateAndAddressId(@NotNull Date calcDate, @NotNull Long addressId);

	/**
	 * Get tariff calculation result list for calculation date and building id
	 * @param calcDate  tariff calculation result date
	 * @param buildingStub tariff calculation result building
	 * @return tariff calculation result list
	 */
	@Secured(Roles.TARIFF_CALCULATION_RESULT_READ)
	List<TariffCalculationResult> getTariffCalcResultsByCalcDateAndBuilding(@NotNull Date calcDate, @NotNull Stub<Building> buildingStub);

	@Secured(Roles.TARIFF_CALCULATION_RESULT_READ)
	List<Date> getUniqueDates();

	@Secured(Roles.TARIFF_CALCULATION_RESULT_READ)
	List<Long> getAddressIds(Date calcDate);

}
