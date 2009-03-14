package org.flexpay.tc.dao.impl;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.flexpay.tc.dao.TariffExportCodeDaoExt;
import org.flexpay.tc.persistence.TariffExportCode;

import java.util.List;

public class TariffExportCodeDaoExtImpl extends HibernateDaoSupport implements TariffExportCodeDaoExt {

	/**
	 * Find Tariff Export Code bu code
	 * @param code code to find
	 * @return TariffExportCode
	 */
	public TariffExportCode findByCode(int code) {
		List<?> tariffs = (List<?>) getHibernateTemplate().findByNamedQuery("TariffExportCode.findByCode", code);
		return tariffs.isEmpty() ? null : (TariffExportCode) tariffs.get(0);
	}
}
