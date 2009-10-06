package org.flexpay.tc.process;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.job.Job;
import org.flexpay.tc.locking.Resources;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.process.exporters.Exporter;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TariffCalcResultExportForBuildingJob extends Job {

	private LockManager lockManager;
	private TariffCalculationResultService tariffCalculationResultService;
	private List<String> subServiceExportCodes;
	private BuildingService buildingService;
	private Exporter exporter;
	private TariffService tariffService;
	/**
	 * Building ID
	 */
	public final static String BUILDING_ID = "BUILDING_ID";

	/**
	 * Tariff calculation date
	 */
	public final static String CALCULATION_DATE = "date";
	/**
	 * Period begin date
	 */
	public final static String PERIOD_BEGIN_DATE = "PERIOD_BEGIN_DATE";

	@Override
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		try {

			Date calculationDate = (Date) parameters.get(CALCULATION_DATE);
			Date periodBeginDate = (Date) parameters.get(PERIOD_BEGIN_DATE);
			Stub<Building> buildingStub = new Stub<Building>(Long.parseLong((String) parameters.get(BUILDING_ID)));

			log.info("Tariff calculation result export procces started");
			log.info("Calculation date := {} building id := {}", calculationDate, buildingStub.getId());

			if (!lockManager.lock(Resources.BUILDING_ATTRIBUTES)) {
				log.info("Another process has already requested a lock and is working");
				return RESULT_NEXT;
			}

			List<TariffCalculationResult> tariffCalcResultList = tariffCalculationResultService.getTariffCalcResultsByCalcDateAndBuilding(
					calculationDate, buildingStub);
			log.info("{} tariff calculation result(s) founded for building with id := {} on date := {}",
					new Object[]{tariffCalcResultList.size(), buildingStub.getId(), calculationDate});
			if (tariffCalcResultList.isEmpty()) {
				log.info("No Tariff calculation results found.");
			} else {
				exporter.beginExport();
				for (String subServiceCode : subServiceExportCodes) {
					TariffCalculationResult tcr = getTariffCalculationResultBySubserviceCode(tariffCalcResultList, subServiceCode);
					if (tcr == null) {
						tcr = new TariffCalculationResult();
						tcr.setBuilding(buildingService.findBuilding(new Stub<BuildingAddress>(buildingStub.getId())));
						tcr.setTariff(tariffService.getTariffByCode(subServiceCode));
						tcr.setCalculationDate(calculationDate);
						tcr.setValue(BigDecimal.ZERO);
					}
					exporter.export(new Object[]{tcr, periodBeginDate});
				}
			}
			exporter.commit();
		} catch (Exception e) {
			log.error("Exception occured", e);
		} finally {
			exporter.endExport();
			lockManager.releaseLock(Resources.BUILDING_ATTRIBUTES);
		}

		log.info("Tariff calculation result export procces finished");

		return RESULT_NEXT;
	}

	private TariffCalculationResult getTariffCalculationResultBySubserviceCode(@NotNull List<TariffCalculationResult> tcrl, @NotNull String subserviceCode) {
		for (TariffCalculationResult tcr : tcrl) {
			if (subserviceCode.equals(tcr.getTariff().getSubServiceCode())) {
				tcrl.remove(tcr);
				return tcr;
			}
		}
		return null;
	}

	@Required
	public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
		this.tariffCalculationResultService = tariffCalculationResultService;
	}

	@Required
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	@Required
	public void setExporter(Exporter exporter) {
		this.exporter = exporter;
	}

	@Required
	public void setSubServiceExportCodes(List<String> subServiceExportCodes) {
		this.subServiceExportCodes = subServiceExportCodes;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setTariffService(TariffService tariffService) {
		this.tariffService = tariffService;
	}

}
