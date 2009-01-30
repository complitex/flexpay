package org.flexpay.tc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.tc.persistence.TariffCalculationResult;

import java.util.Date;
import java.util.List;

public interface TariffCalculationResultDao extends GenericDao<TariffCalculationResult, Long> {

	List<Date> findUniqueDates();

	List<Long> findBuildingAddressIdsByCalcDate(Date calcDate);

	List<TariffCalculationResult> findByCalcDateAndAddressId(Date calcDate, Long addressId);

}
