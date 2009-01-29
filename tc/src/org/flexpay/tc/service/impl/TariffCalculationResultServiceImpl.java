package org.flexpay.tc.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.dao.TariffCalculationResultDao;
import org.flexpay.tc.dao.TariffCalculationResultDaoExt;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TariffCalculationResultServiceImpl implements TariffCalculationResultService {

	private TariffCalculationResultDao tariffCalculationResultDao;

	@Transactional (readOnly = false)
	public void add(@NotNull TariffCalculationResult tariffCalculationResult) {
		tariffCalculationResult.setId(null);
		tariffCalculationResultDao.create(tariffCalculationResult);
	}

	public TariffCalculationResult read(@NotNull Stub<TariffCalculationResult> stub) {
		return tariffCalculationResultDao.readFull(stub.getId());
	}

	public List<TariffCalculationResult> getTariffCalcResultsByCalcDateAndBuildingId(@NotNull Date calcDate, @NotNull Long buildingId) {
		return tariffCalculationResultDao.findByCalcDateAndBuildingId(calcDate, buildingId);
	}

	public List<Date> getUniqueDates() {
		return tariffCalculationResultDao.findUniqueDates();
	}

	@Required
	public void setTariffCalculationResultDao(TariffCalculationResultDao tariffCalculationResultDao) {
		this.tariffCalculationResultDao = tariffCalculationResultDao;
	}

}
