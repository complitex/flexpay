package org.flexpay.tc.service.impl;

import org.flexpay.tc.service.TariffCalcRegulationService;
import org.flexpay.tc.persistence.TariffCalcRegulation;
import org.flexpay.tc.dao.TariffCalcRegulationDao;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TariffCalcRegulationServiceImpl implements TariffCalcRegulationService {

	private TariffCalcRegulationDao tariffCalcRegulationDao;

	@Transactional (readOnly = false)
	public void save(@NotNull TariffCalcRegulation tariffCalcRegulation) {
		if (tariffCalcRegulation.isNew()) {
			tariffCalcRegulation.setId(null);
			tariffCalcRegulationDao.create(tariffCalcRegulation);
		} else {
			tariffCalcRegulationDao.update(tariffCalcRegulation);
		}
	}

	public TariffCalcRegulation read(@NotNull Stub<TariffCalcRegulation> stub) {
		return tariffCalcRegulationDao.readFull(stub.getId());
	}

	@Transactional (readOnly = false)
	public void disable(@NotNull Set<Long> objectIds) {
		for (Long id : objectIds) {
			TariffCalcRegulation tariffCalcRegulation = tariffCalcRegulationDao.read(id);
			if (tariffCalcRegulation != null) {
				tariffCalcRegulation.disable();
				tariffCalcRegulationDao.update(tariffCalcRegulation);
			}
		}
	}

	@Required
	public void setTariffCalcRegulationDao(TariffCalcRegulationDao tariffCalcRegulationDao) {
		this.tariffCalcRegulationDao = tariffCalcRegulationDao;
	}

}
