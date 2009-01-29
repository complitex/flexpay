package org.flexpay.tc.dao.impl;

import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.tc.dao.TariffCalculationResultDaoExt;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class TariffCalculationResultDaoExtImpl extends HibernateDaoSupport implements TariffCalculationResultDaoExt {

	@SuppressWarnings({"unchecked"})
	public String findExternalId(Long internalId, int type, DataSourceDescription ds) {
		Object[] params = {internalId, type, ds};
		List<String> list = (List<String>) getHibernateTemplate().findByNamedQuery("TariffCalculationResult.findExternalId", params);
		return list.isEmpty() ? null : list.get(0);
	}

}
