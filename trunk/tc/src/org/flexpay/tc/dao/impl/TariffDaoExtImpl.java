package org.flexpay.tc.dao.impl;

import org.flexpay.tc.dao.TariffDaoExt;
import org.flexpay.tc.persistence.Tariff;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class TariffDaoExtImpl extends HibernateDaoSupport implements TariffDaoExt {

	/**
	 * Find tariff by code
	 *
	 * @param code tariff code
	 * @return tariff
	 */
	@Override
	public Tariff getTariffByCode(@NotNull String code) {
		List<?> tariffs = (List<?>) getHibernateTemplate().findByNamedQuery("Tariff.getByCode", code);
		return tariffs.isEmpty() ? null : (Tariff) tariffs.get(0);
	}

}
