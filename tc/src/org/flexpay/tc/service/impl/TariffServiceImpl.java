package org.flexpay.tc.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.dao.TariffDao;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.service.TariffService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TariffServiceImpl implements TariffService {

	private TariffDao tariffDao;

	@Transactional (readOnly = false)
	public void save(@NotNull Tariff tariff) {
		if (tariff.isNew()) {
			tariff.setId(null);
			tariffDao.create(tariff);
		} else {
			tariffDao.update(tariff);
		}
	}

	public Tariff getTariff(@NotNull Stub<Tariff> stub) {
		return tariffDao.readFull(stub.getId());
	}

	@Transactional (readOnly = false)
	public void disable(@NotNull Set<Long> objectIds) {
		for (Long id : objectIds) {
			Tariff tariff = tariffDao.read(id);
			if (tariff != null) {
				tariff.disable();
				tariffDao.update(tariff);
			}
		}
	}

	@Required
	public void setTariffDao(TariffDao tariffDao) {
		this.tariffDao = tariffDao;
	}

}
