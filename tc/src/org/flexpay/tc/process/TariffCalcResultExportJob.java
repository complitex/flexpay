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
import org.flexpay.tc.process.exporters.Exporter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TariffCalcResultExportJob extends Job {

	private Long dataSourceDescriptionId;
	private LockManager lockManager;
	private ClassToTypeRegistry classToTypeRegistry;
	private CorrectionsService correctionsService;
	private TariffCalculationResultService tariffCalculationResultService;
	private Exporter exporter;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		Logger pLog = ProcessLogger.getLogger(getClass());
		
		pLog.debug("Tariff calculation result export procces started");

		if (!lockManager.lock(Resources.BUILDING_ATTRIBUTES)) {
			log.info("Another process has already requested a lock and is working");
			return RESULT_NEXT;
		}

//		Connection conn = null;
		try {
			exporter.beginExport();

			Date calcDate = (Date) parameters.get("date");

			List<Long> addressIds = tariffCalculationResultService.getAddressIds(calcDate);

			for (Long addressId : addressIds) {
				
				String externalId = correctionsService.getExternalId(addressId, classToTypeRegistry.getType(BuildingAddress.class), dataSourceDescriptionId);
				if (externalId == null) {
					pLog.debug("ExternalId for building address with id={} not found", addressId);
					continue;
				}

				Integer intExtId = Integer.parseInt(externalId);
				List<TariffCalculationResult> tcrs = tariffCalculationResultService.getTariffCalcResultsByCalcDateAndAddressId(calcDate, addressId);

				try {
					for (TariffCalculationResult tcr : tcrs) {
						exporter.export(new Object[]{tcr, intExtId});
					}
					exporter.commit();
				} catch (FlexPayException  e) {
					pLog.error("SQL error for adressId=" + addressId, e);
					try {
						exporter.rollback();
					} catch (FlexPayException ex) {
						ex.printStackTrace();
						// do nothing
					}
				}
			}
		} catch (Exception e) {
			pLog.error("Exporter exception", e);
		} finally {
			try{
				exporter.endExport();
			}catch(Exception e){
				e.printStackTrace();
			}
			lockManager.releaseLock(Resources.BUILDING_ATTRIBUTES);
		}
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

	@Required
	public void setExporter(Exporter exporter) {
		this.exporter = exporter;
	}
}
