package org.flexpay.tc.service.importexport;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.importexport.imp.ClassToTypeRegistry;
import org.flexpay.common.dao.DataSourceDescriptionDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.tc.dao.TariffCalculationResultDao;
import org.flexpay.tc.dao.TariffCalculationResultDaoExt;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
public class TariffCalculationResultsExportService {

	private ClassToTypeRegistry classToTypeRegistry;
	private DataSourceDescriptionDao dataSourceDescriptionDao;
	private TariffCalculationResultDao tariffCalculationResultDao;
	private TariffCalculationResultDaoExt tariffCalculationResultDaoExt;

	@NonNls
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	public DataSourceDescription getDataSourceDescription(Long id) {
		return dataSourceDescriptionDao.readFull(id);
	}

	public int export(Date calcDate, DataSourceDescription ds) throws FlexPayException {

		Logger pLog = ProcessLogger.getLogger(getClass());

		Page<Building> page = new Page<Building>(100, 1);

		while (page.getPageNumber() <= page.getLastPageNumber()) {

			List<Building> buildings = tariffCalculationResultDao.findBuildingsByCalcDate(calcDate, page);

			for (Building building : buildings) {
				String externalId = tariffCalculationResultDaoExt.findExternalId(building.getBuildingses().iterator().next().getId(),
							classToTypeRegistry.getType(BuildingAddress.class), ds);
				List<TariffCalculationResult> calcResults = tariffCalculationResultDao.findByCalcDateAndBuildingId(calcDate, building.getId());

				for (TariffCalculationResult calcResult : calcResults) {

					int exportResult = 1;
					externalId = externalId;
					calcResult.getTariff().getSubServiceCode();
					calcResult.getValue();
					calcResult.getCalculationDate();

					if (exportResult == 0) {
						pLog.warn("Tariff {} for building with id={} not exists", calcResult.getTariff(), calcResult.getBuilding().getId());
					} else if (exportResult == -1) {
						pLog.warn("Building with id={} for caluculation result {} not found", calcResult.getBuilding().getId(), calcResult);
					} else if (exportResult == -2) {
						pLog.warn("Error: Can't create record in hixtory for calculation result {}", calcResult);
					} else if (exportResult == -3) {
						pLog.warn("Locking exception: Can't export calculcation result {}", calcResult);
					} else {
						pLog.info("Tariff calculation result {} exported succesfully", calcResult);
					}

				}

			}

			page.setPageNumber(page.getPageNumber() + 1);
		}

		return -1;
	}

	@Required
	public void setDataSourceDescriptionDao(DataSourceDescriptionDao dataSourceDescriptionDao) {
		this.dataSourceDescriptionDao = dataSourceDescriptionDao;
	}

	@Required
	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}

	@Required
	public void setTariffCalculationResultDao(TariffCalculationResultDao tariffCalculationResultDao) {
		this.tariffCalculationResultDao = tariffCalculationResultDao;
	}

	@Required
	public void setTariffCalculationResultDaoExt(TariffCalculationResultDaoExt tariffCalculationResultDaoExt) {
		this.tariffCalculationResultDaoExt = tariffCalculationResultDaoExt;
	}

}
