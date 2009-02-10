package org.flexpay.tc.service;

import org.springframework.security.annotation.Secured;
import org.flexpay.tc.persistence.Tariff;
import org.jetbrains.annotations.NotNull;

public interface TariffServiceExt {

	/**
	 * Find tariff by code
	 * @param code tariff code
	 * @return tarif
	 */	
	@Secured ({Roles.TARIFF_READ})
	Tariff getTariffByCode(@NotNull String code);

}
