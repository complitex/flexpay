package org.flexpay.tc.process;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.tc.service.importexport.TariffCalculationResultsExportService;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class TariffCalcResultExportJob extends Job {

	@NonNls
	private Logger pLog = ProcessLogger.getLogger(getClass());

	private Long dataSourceDescriptionId;
	private LockManager lockManager;
	private TariffCalculationResultsExportService tariffCalculationResultsExportService;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		pLog.debug("Tariff calculation result export procces started");

		if (!lockManager.lock("calc_result_export_tc_lock")) {
			log.info("Another process has already requested a lock and is working");
			return RESULT_NEXT;
		}

		Date calcDate = (Date) parameters.get("date");
		DataSourceDescription ds = tariffCalculationResultsExportService.getDataSourceDescription(dataSourceDescriptionId);
		tariffCalculationResultsExportService.export(calcDate, ds);

		lockManager.releaseLock("calc_result_export_tc_lock");

		pLog.debug("Tariff calculation result export procces finished");

		return RESULT_NEXT;
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
	public void setTariffCalculationResultsExportService(TariffCalculationResultsExportService tariffCalculationResultsExportService) {
		this.tariffCalculationResultsExportService = tariffCalculationResultsExportService;
	}

}
