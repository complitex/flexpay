package org.flexpay.tc.dao;

import org.flexpay.tc.persistence.TariffExportCode;

public interface TariffExportCodeDaoExt {

	/**
	 * Find Tariff Export Code bu code
	 *
	 * @param code code to find
	 * @return TariffExportCode
	 */
	public TariffExportCode findByCode(int code);
}
