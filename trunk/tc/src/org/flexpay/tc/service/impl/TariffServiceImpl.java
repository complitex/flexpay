package org.flexpay.tc.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.dao.TariffDao;
import org.flexpay.tc.dao.TariffDaoExt;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.service.TariffService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class TariffServiceImpl implements TariffService {

	private TariffDao tariffDao;

	private TariffDaoExt tariffServiceDaoExt;

	public Tariff getTariffByCode(@NotNull String code) {
		return tariffServiceDaoExt.getTariffByCode(code);
	}

	@Transactional (readOnly = false)
	public void create(@NotNull Tariff tariff) {
		tariff.setId(null);
		tariffDao.create(tariff);
	}

	@Transactional (readOnly = false)
	public void update(@NotNull Tariff tariff) {
		tariffDao.update(tariff);
	}

	public Tariff readFull(@NotNull Stub<Tariff> stub) {
		return tariffDao.readFull(stub.getId());
	}

	public List<Tariff> listTariffs() {
		return tariffDao.listTariffs();
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

	@Required
	public void setTariffServiceDaoExt(TariffDaoExt tariffServiceDaoExt) {
		this.tariffServiceDaoExt = tariffServiceDaoExt;
	}
}
