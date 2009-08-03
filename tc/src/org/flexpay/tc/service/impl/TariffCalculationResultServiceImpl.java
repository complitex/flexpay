package org.flexpay.tc.service.impl;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.dao.TariffCalculationResultDao;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class TariffCalculationResultServiceImpl implements TariffCalculationResultService {

	private TariffCalculationResultDao tariffCalculationResultDao;

	/**
	 * Save tariff calculation results for building
	 *
	 * @param tariffCalculationResult calculated result
	 */
	@Transactional (readOnly = false)
	public void add(@NotNull TariffCalculationResult tariffCalculationResult) {
		tariffCalculationResult.setId(null);
		tariffCalculationResultDao.create(tariffCalculationResult);
	}

	@Transactional (readOnly = false)
	public void add(@NotNull Set<TariffCalculationResult> tariffCalculationResults) {
		for (TariffCalculationResult tcr : tariffCalculationResults) {
			tariffCalculationResultDao.create(tcr);
		}
	}

	/**
	 * Save tariff calculation results for building
	 *
	 * @param value		   calculated value
	 * @param creationDate	record creation date
	 * @param calculationDate record calculation date
	 * @param building		building
	 * @param tariff		  tariff
	 */
	@Transactional (readOnly = false)
	public void add(@NotNull BigDecimal value, Date creationDate, Date calculationDate,
				 @NotNull Building building, @NotNull Tariff tariff) {
		TariffCalculationResult tariffCalculationResult = new TariffCalculationResult(
				value, creationDate, calculationDate, building, tariff);
		add(tariffCalculationResult);
	}

	/**
	 * Update tariff calculation result
	 *
	 * @param result result to be updated
	 * @return updated instance
	 */
	@Transactional (readOnly = false)
	public TariffCalculationResult update(TariffCalculationResult result) {
		tariffCalculationResultDao.update(result);
		return result;
	}

	/**
	 * Get tariff calculation result list for calculation date and building
	 *
	 * @param calcDate	 tariff calculation result date
	 * @param buildingStub tariff calculation result building
	 * @return tariff calculation result list
	 */
	public List<TariffCalculationResult> getTariffCalcResultsByCalcDateAndBuilding(
			@NotNull Date calcDate, @NotNull Stub<Building> buildingStub) {
		return tariffCalculationResultDao.findByCalcDateAndBuilding(calcDate, buildingStub.getId());
	}

	public TariffCalculationResult findTariffCalcResults(
			@NotNull Date calcDate, @NotNull Stub<Tariff> tariffStub, @NotNull Stub<Building> buildingStub)
			throws FlexPayException {
		List<TariffCalculationResult> results = tariffCalculationResultDao
				.findByCalcDateTariffAndBuilding(calcDate, tariffStub.getId(), buildingStub.getId());

		if (results.size() > 1) {
			throw new FlexPayException("Unexpected data fetch result. There should be only one " +
									   "tariff calculation result for calculation date, tariff and building");
		}

		return results.get(0);
	}

	public TariffCalculationResult read(@NotNull Stub<TariffCalculationResult> stub) {
		return tariffCalculationResultDao.readFull(stub.getId());
	}

	public List<TariffCalculationResult> getTariffCalcResultsByCalcDateAndAddressId(
			@NotNull Date calcDate, @NotNull Stub<BuildingAddress> addressStub) {
		return tariffCalculationResultDao.findByCalcDateAndAddressId(calcDate, addressStub.getId());
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
