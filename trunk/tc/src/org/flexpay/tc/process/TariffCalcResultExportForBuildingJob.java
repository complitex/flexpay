package org.flexpay.tc.process;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.importexport.imp.ClassToTypeRegistry;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.tc.locking.Resources;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.process.exporters.Exporter;
import org.flexpay.tc.service.TariffCalculationResultService;
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
	private Exporter exporter;
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

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		try {

			Date calculationDate = (Date) parameters.get(CALCULATION_DATE);
			Date periodBeginDate = (Date) parameters.get(PERIOD_BEGIN_DATE);
			Stub<Building> buildingStub = new Stub<Building>(Long.parseLong((String)parameters.get(BUILDING_ID)));

			log.info("Tariff calculation result export procces started");
			log.info("Calculation date := {} building id := {}", calculationDate, buildingStub.getId());

			if (!lockManager.lock(Resources.BUILDING_ATTRIBUTES)) {
				log.info("Another process has already requested a lock and is working");
				return RESULT_NEXT;
			}

			List<TariffCalculationResult> tariffCalcResultList = tariffCalculationResultService.getTariffCalcResultsByCalcDateAndBuilding(
					calculationDate, buildingStub);
			log.info("{} tariff calculation result(s) founded for building with id := {} on date := {}",
					new Object[] {tariffCalcResultList.size(), buildingStub.getId(), calculationDate});
			if (tariffCalcResultList.size() > 0) {
				exporter.beginExport();
				for (TariffCalculationResult tcr : tariffCalcResultList) {
					exporter.export(new Object[]{tcr, periodBeginDate});
					subServiceExportCodes.remove(tcr.getTariff().getSubServiceCode());
				}
				for(String code : subServiceExportCodes){
					TariffCalculationResult tcr = new TariffCalculationResult();
					Tariff tariff = new Tariff(); tariff.setSubServiceCode(code);
					tcr.setTariff(tariff);
					tcr.setCalculationDate(calculationDate);
					tcr.setValue(BigDecimal.ZERO);
					exporter.export(new Object[]{tcr,periodBeginDate});
				}

			}else{
				log.info("No Tariff calculation results found.");
			}
			exporter.commit();
		} catch (Exception e){
			log.error("Exception occured", e);
		}finally {
			exporter.endExport();
			lockManager.releaseLock(Resources.BUILDING_ATTRIBUTES);
		}

		log.info("Tariff calculation result export procces finished");

		return RESULT_NEXT;
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

}
