package org.flexpay.tc.dao.impl;

import org.flexpay.tc.dao.TariffExportCodeDaoExt;
import org.flexpay.tc.persistence.TariffExportCode;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import java.util.List;

public class TariffExportCodeDaoExtImpl extends JpaDaoSupport implements TariffExportCodeDaoExt {

	/**
	 * Find Tariff Export Code bu code
	 *
	 * @param code code to find
	 * @return TariffExportCode
	 */
	@Override
	public TariffExportCode findByCode(int code) {
		List<?> tariffs = (List<?>) getJpaTemplate().findByNamedQuery("TariffExportCode.findByCode", code);
		return tariffs.isEmpty() ? null : (TariffExportCode) tariffs.get(0);
	}

}
