package org.flexpay.tc.service.impl;

import org.flexpay.tc.service.TariffService;
import org.flexpay.tc.service.TariffServiceExt;
import org.flexpay.tc.service.Roles;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.dao.TariffDaoExt;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

public class TariffServiceExtImpl implements TariffServiceExt{
	
	private TariffDaoExt tariffServiceDaoExt;

	@Secured (Roles.TARIFF_READ)
	public Tariff getTariffByCode(@NotNull String code) {
		return tariffServiceDaoExt.getTariffByCode(code);
	}

	public void setTariffServiceDaoExt(TariffDaoExt tariffServiceDaoExt) {
		this.tariffServiceDaoExt = tariffServiceDaoExt;
	}
}
