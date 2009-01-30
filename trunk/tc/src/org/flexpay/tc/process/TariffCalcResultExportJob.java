package org.flexpay.tc.process;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.importexport.imp.ClassToTypeRegistry;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.tc.locking.Resources;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TariffCalcResultExportJob extends Job {

	@NonNls
	private Logger pLog = ProcessLogger.getLogger(getClass());

	private Long dataSourceDescriptionId;
	private LockManager lockManager;
	private ClassToTypeRegistry classToTypeRegistry;
	private CorrectionsService correctionsService;
	private TariffCalculationResultService tariffCalculationResultService;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		pLog.debug("Tariff calculation result export procces started");

		if (!lockManager.lock(Resources.BUILDING_ATTRIBUTES)) {
			log.info("Another process has already requested a lock and is working");
			return RESULT_NEXT;
		}

		Date calcDate = (Date) parameters.get("date");

		log.debug("CalcDate - {}", calcDate);

		List<Long> addressIds = tariffCalculationResultService.getAddressIds(calcDate);

		log.debug("AdressIds - {}", addressIds);

		for (Long addressId : addressIds) {

			String externalId = correctionsService.getExternalId(addressId, classToTypeRegistry.getType(BuildingAddress.class), dataSourceDescriptionId);
			if (externalId == null) {
				pLog.debug("ExternalId for building address with id={} not found", addressId);
				continue;
			}
			log.debug("InternalId - {}, ExternalId - {}", addressId, externalId);

			List<TariffCalculationResult> tcrs = tariffCalculationResultService.getTariffCalcResultsByCalcDateAndAddressId(calcDate, addressId);

			log.debug("TCRs - {}", tcrs);

			for (TariffCalculationResult tcr : tcrs) {
				//TODO: Replace '1' to setFactTariff stored procedure
				int exportResult = 1;

				if (exportResult == 0) {
					pLog.warn("Tariff {} for building with id={} not exists", tcr.getTariff(), tcr.getBuilding().getId());
				} else if (exportResult == -1) {
					pLog.warn("Building with id={} for caluculation result {} not found", tcr.getBuilding().getId(), tcr);
				} else if (exportResult == -2) {
					pLog.warn("Error: Can't create record in history for calculation result {}", tcr);
				} else if (exportResult == -3) {
					pLog.warn("Locking exception: Can't export calculcation result {}", tcr);
				} else {
					pLog.debug("Tariff calculation result {} exported succesfully", tcr);
				}

			}

		}

		lockManager.releaseLock(Resources.BUILDING_ATTRIBUTES);

		pLog.debug("Tariff calculation result export procces finished");

		return RESULT_NEXT;
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

}
