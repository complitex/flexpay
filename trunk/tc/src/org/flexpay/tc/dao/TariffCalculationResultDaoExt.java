package org.flexpay.tc.dao;

import org.flexpay.common.persistence.DataSourceDescription;

public interface TariffCalculationResultDaoExt {

	String findExternalId(Long internalId, int type, DataSourceDescription ds);

}
