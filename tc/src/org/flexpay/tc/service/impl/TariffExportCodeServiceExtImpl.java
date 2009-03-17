package org.flexpay.tc.service.impl;

import org.flexpay.tc.dao.TariffExportCodeDaoExt;
import org.flexpay.tc.persistence.TariffExportCode;
import org.flexpay.tc.service.TariffExportCodeServiceExt;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class TariffExportCodeServiceExtImpl implements TariffExportCodeServiceExt {

	TariffExportCodeDaoExt tariffExportCodeDaoExt;

	/**
	 * Find Tariff Export Code bu code
	 *
	 * @param code code to find
	 * @return TariffExportCode
	 */
	public TariffExportCode findByCode(int code) {
		return tariffExportCodeDaoExt.findByCode(code);
	}

	@Required
	public void setTariffExportCodeDaoExt(TariffExportCodeDaoExt tariffExportCodeDaoExt) {
		this.tariffExportCodeDaoExt = tariffExportCodeDaoExt;
	}
}
