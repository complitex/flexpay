package org.flexpay.tc.service.impl;

import org.flexpay.tc.dao.TariffDaoExt;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.service.TariffServiceExt;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class TariffServiceExtImpl implements TariffServiceExt {

	private TariffDaoExt tariffServiceDaoExt;

	public Tariff getTariffByCode(@NotNull String code) {
		return tariffServiceDaoExt.getTariffByCode(code);
	}

	public void setTariffServiceDaoExt(TariffDaoExt tariffServiceDaoExt) {
		this.tariffServiceDaoExt = tariffServiceDaoExt;
	}
}
