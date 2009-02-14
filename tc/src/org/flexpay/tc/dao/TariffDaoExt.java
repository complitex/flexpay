package org.flexpay.tc.dao;

import org.flexpay.tc.persistence.Tariff;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TariffDaoExt {

	/**
	 * Find tariff by code
	 *
	 * @param code tariff code
	 * @return tarif
	 */
	@Nullable
	Tariff getTariffByCode(@NotNull String code);
}
