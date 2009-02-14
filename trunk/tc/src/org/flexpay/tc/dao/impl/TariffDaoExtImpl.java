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
	 * @return tarif
	 */
	@SuppressWarnings ({"unchecked"})
	public Tariff getTariffByCode(@NotNull String code) {
		List<Tariff> tariffs = (List<Tariff>) getHibernateTemplate().findByNamedQuery("Tariff.getByCode", code);
		if (tariffs.size() == 0) {
			return null;
		} else {
			return tariffs.get(0);
		}
	}

}
