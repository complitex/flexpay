package org.flexpay.tc.dao.impl;

import org.flexpay.tc.dao.TariffDaoExt;
import org.flexpay.tc.persistence.Tariff;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TariffDaoExtImpl extends HibernateDaoSupport implements TariffDaoExt {

	/**
	 * Find tariff by code
	 * @param code tariff code
	 * @return tarif
	 */	
	public Tariff getTariffByCode(@NotNull String code) {
		List<Tariff> tariffList = (List<Tariff>)getHibernateTemplate().findByNamedQuery("Tariff.getByCode", code);
		if (tariffList.size() == 0){
			return null;
		}else{
			return  tariffList.get(0);
		}
	}

}
