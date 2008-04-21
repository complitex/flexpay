package org.flexpay.ab.dao;

import java.util.List;
import java.util.Date;

import org.flexpay.ab.persistence.Street;
import org.flexpay.common.dao.NameTimeDependentDao;

public interface StreetDaoExt {

	void invalidateTypeTemporals(Long streetId, Date futureInfinity, Date invalidDate);
}