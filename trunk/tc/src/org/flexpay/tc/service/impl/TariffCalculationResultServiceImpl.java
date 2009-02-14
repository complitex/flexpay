package org.flexpay.tc.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.dao.TariffCalculationResultDao;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.ab.persistence.Building;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TariffCalculationResultServiceImpl implements TariffCalculationResultService {

	private TariffCalculationResultDao tariffCalculationResultDao;

	/**
	 * Save tariff calculation results for building
	 * @param tariffCalculationResult calculated result
	 */
	@Transactional (readOnly = false)
	public void add(@NotNull TariffCalculationResult tariffCalculationResult) {
		tariffCalculationResult.setId(null);
		tariffCalculationResultDao.create(tariffCalculationResult);
	}

	/**
	 * Save tariff calculation results for building
	 * @param value calculated value
	 * @param creationDate record creation date
	 * @param calculationDate record calculation date
	 * @param building building
	 * @param tariff tariff
	 */
	@Transactional (readOnly = false)
	public void add(@NotNull BigDecimal value, Date creationDate, Date calculationDate, @NotNull Building building, @NotNull Tariff tariff){
		TariffCalculationResult tariffCalculationResult = new TariffCalculationResult(value, creationDate, calculationDate, building, tariff);
		add(tariffCalculationResult);
	}

	/**
	 * Get tariff calculation result list for calculation date and building
	 *
	 * @param calcDate	 tariff calculation result date
	 * @param buildingStub tariff calculation result building
	 * @return tariff calculation result list
	 */
	public List<TariffCalculationResult> getTariffCalcResultsByCalcDateAndBuilding(@NotNull Date calcDate, @NotNull Stub<Building> buildingStub) {
		return tariffCalculationResultDao.findByCalcDateAndBuilding(calcDate, buildingStub.getId());
	}

	public TariffCalculationResult read(@NotNull Stub<TariffCalculationResult> stub) {
		return tariffCalculationResultDao.readFull(stub.getId());
	}

	public List<TariffCalculationResult> getTariffCalcResultsByCalcDateAndAddressId(@NotNull Date calcDate, @NotNull Long addressId) {
		return tariffCalculationResultDao.findByCalcDateAndAddressId(calcDate, addressId);
	}

	public List<Date> getUniqueDates() {
		return tariffCalculationResultDao.findUniqueDates();
	}

	public List<Long> getAddressIds(Date calcDate) {
		return tariffCalculationResultDao.findBuildingAddressIdsByCalcDate(calcDate);
	}

	@Required
	public void setTariffCalculationResultDao(TariffCalculationResultDao tariffCalculationResultDao) {
		this.tariffCalculationResultDao = tariffCalculationResultDao;
	}

}
