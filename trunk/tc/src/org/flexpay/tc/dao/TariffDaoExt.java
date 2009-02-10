package org.flexpay.tc.dao;

import org.flexpay.tc.persistence.Tariff;
import org.jetbrains.annotations.NotNull;

public interface TariffDaoExt {

	/**
	 * Find tariff by code
	 * @param code tariff code
	 * @return tarif 
	 */
	public Tariff getTariffByCode(@NotNull String code);
	
}
