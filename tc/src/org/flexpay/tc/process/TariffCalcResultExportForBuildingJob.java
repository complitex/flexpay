package org.flexpay.tc.process;

import org.flexpay.common.process.job.Job;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.ab.service.importexport.imp.ClassToTypeRegistry;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.locking.Resources;
import org.flexpay.tc.process.exporters.Exporter;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import java.util.List;

public class TariffCalcResultExportForBuildingJob extends Job {

	private Long dataSourceDescriptionId;
	private LockManager lockManager;
	private ClassToTypeRegistry classToTypeRegistry;
	private CorrectionsService correctionsService;
	private TariffCalculationResultService tariffCalculationResultService;
	private BuildingService buildingService;

	private Exporter exporter;
	/**
	 * Building ID
	 */
	public final static String BUILDING_ID = "BUILDING_ID";

	/**
	 * Tarif calculation date
	 */
	public final static String CALCULATION_DATE = "date";

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

//		Logger pLog = ProcessLogger.getLogger(getClass());
		try {

			Date calculationDate = (Date) parameters.get(CALCULATION_DATE);
			Stub<Building> buildingStub = new Stub<Building>(Long.parseLong((String)parameters.get(BUILDING_ID)));

			log.info("Tariff calculation result export procces started");
			log.info("Calculation date := {} building id := {}", new Object[]{calculationDate, buildingStub.getId()});

			if (!lockManager.lock(Resources.BUILDING_ATTRIBUTES)) {
				log.info("Another process has already requested a lock and is working");
				return RESULT_NEXT;
			}

			List<TariffCalculationResult> tariffCalcResultList = tariffCalculationResultService.getTariffCalcResultsByCalcDateAndBuilding(
					calculationDate, buildingStub);
			//find external id
			log.info("{} tarif calculation result(s) founded for building with id := {} on date := {}", new Object[]{tariffCalcResultList.size(), buildingStub.getId(), calculationDate});
			if (tariffCalcResultList.size() > 0) {
				exporter.beginExport();
				String externalId = getExternalId(buildingStub);
				for (TariffCalculationResult trc : tariffCalcResultList) {
					exporter.export(new Object[]{trc, externalId});
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

	private String getExternalId(@NotNull Stub<Building> buildingStub) throws FlexPayException {

		List<BuildingAddress> buildingAddressList = buildingService.getBuildingBuildings(buildingStub);
		for (BuildingAddress buildingAddress : buildingAddressList) {
			String externalId = correctionsService.getExternalId(buildingAddress.getId(), classToTypeRegistry.getType(BuildingAddress.class), dataSourceDescriptionId);
			if (externalId != null) {
				return externalId;
			}
		}
		throw new FlexPayException("Building adress not found for building.id=" + buildingStub.getId());
	}

	@Required
	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
		this.tariffCalculationResultService = tariffCalculationResultService;
	}

	@Required
	public void setDataSourceDescriptionId(Long dataSourceDescriptionId) {
		this.dataSourceDescriptionId = dataSourceDescriptionId;
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
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}
