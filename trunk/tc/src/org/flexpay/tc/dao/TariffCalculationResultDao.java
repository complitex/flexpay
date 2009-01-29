package org.flexpay.tc.dao;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.tc.persistence.TariffCalculationResult;

import java.util.Date;
import java.util.List;

public interface TariffCalculationResultDao extends GenericDao<TariffCalculationResult, Long> {

	List<Date> findUniqueDates();

	List<Building> findBuildingsByCalcDate(Date calcDate, Page<Building> page);

	List<TariffCalculationResult> findByCalcDateAndBuildingId(Date calcDate, Long buildingId);

}
