package org.flexpay.tc.dao.impl;

import org.flexpay.tc.dao.TariffDaoExt;
import org.flexpay.tc.persistence.Tariff;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import java.util.List;

public class TariffDaoExtImpl extends JpaDaoSupport implements TariffDaoExt {

	/**
	 * Find tariff by code
	 *
	 * @param code tariff code
	 * @return tariff
	 */
	@Override
	public Tariff getTariffByCode(@NotNull String code) {
		List<?> tariffs = (List<?>) getJpaTemplate().findByNamedQuery("Tariff.getByCode", code);
		return tariffs.isEmpty() ? null : (Tariff) tariffs.get(0);
	}

}
