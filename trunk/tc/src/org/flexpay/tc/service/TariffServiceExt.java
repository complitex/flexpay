package org.flexpay.tc.service;

import org.flexpay.tc.persistence.Tariff;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

public interface TariffServiceExt {

	/**
	 * Find tariff by code
	 *
	 * @param code tariff code
	 * @return tariff
	 */
	@Secured ({Roles.TARIFF_READ})
	@Nullable
	Tariff getTariffByCode(@NotNull String code);

}
