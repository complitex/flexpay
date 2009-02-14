package org.flexpay.tc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.ab.persistence.Building;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public interface TariffCalculationResultDao extends GenericDao<TariffCalculationResult, Long> {

	List<Date> findUniqueDates();

	List<Long> findBuildingAddressIdsByCalcDate(Date calcDate);

	List<TariffCalculationResult> findByCalcDateAndAddressId(Date calcDate, Long addressId);

	/**
	 * Get tariff calculation result list for calculation date and building id
	 *
	 * @param calcDate	 tariff calculation result date
	 * @param buildingId tariff calculation result building Id
	 * @return tariff calculation result list
	 */
	List<TariffCalculationResult> findByCalcDateAndBuilding(@NotNull Date calcDate, @NotNull Long buildingId);
}
