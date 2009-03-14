package org.flexpay.tc.service;

import org.flexpay.tc.persistence.TariffExportCode;

public interface TariffExportCodeServiceExt {

	/**
	 * Find Tariff Export Code bu code
	 * @param code code to find
	 * @return TariffExportCode
	 */
	public TariffExportCode findByCode(int code);
}
