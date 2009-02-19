package org.flexpay.tc.process;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.importexport.imp.ClassToTypeRegistry;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.tc.locking.Resources;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.process.exporters.Exporter;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

public class TariffCalcResultExportJob extends Job {

	private Long dataSourceDescriptionId;
	private LockManager lockManager;
	private ClassToTypeRegistry classToTypeRegistry;
	private CorrectionsService correctionsService;
	private TariffCalculationResultService tariffCalculationResultService;
	private Exporter exporter;
	public final static String CALCULATION_DATE = "CALCULATION_DATE";
	public final static String PERIOD_BEGIN_DATE = "PERIOD_BEGIN_DATE";
	private List<String> subServiceExportCodes;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

//		Logger pLog = ProcessLogger.getLogger(getClass());

		log.debug("Tariff calculation result export procces started");

		if (!lockManager.lock(Resources.BUILDING_ATTRIBUTES)) {
			log.info("Another process has already requested a lock and is working");
			return RESULT_NEXT;
		}

//		Connection conn = null;
		try {
			exporter.beginExport();

			Date calcDate = (Date) parameters.get(CALCULATION_DATE);
			Date periodBeginDate = (Date) parameters.get(PERIOD_BEGIN_DATE);

			List<Long> addressIds = tariffCalculationResultService.getAddressIds(calcDate);

			for (Long addressId : addressIds) {
				
				String externalId = correctionsService.getExternalId(addressId, classToTypeRegistry.getType(BuildingAddress.class), dataSourceDescriptionId);
				if (externalId == null) {
					log.debug("ExternalId for building address with id={} not found", addressId);
					continue;
				}

				List<TariffCalculationResult> tcrs = tariffCalculationResultService.getTariffCalcResultsByCalcDateAndAddressId(calcDate, addressId);
				List<String> addressSubServiceexportCodes = CollectionUtils.list();
				addressSubServiceexportCodes.addAll(subServiceExportCodes);
				try {
					for (TariffCalculationResult tcr : tcrs) {
						exporter.export(new Object[]{tcr, externalId, periodBeginDate});
						addressSubServiceexportCodes.remove(tcr.getTariff().getSubServiceCode());
					}
					for(String code : addressSubServiceexportCodes){
						TariffCalculationResult tcr = new TariffCalculationResult();
						Tariff tariff = new Tariff(); tariff.setSubServiceCode(code);
						tcr.setTariff(tariff);
						tcr.setCalculationDate(calcDate);
						tcr.setValue(BigDecimal.ZERO);
						exporter.export(new Object[]{tcr,externalId, periodBeginDate});
					}
					exporter.commit();
				} catch (FlexPayException  e) {
					log.error("SQL error for addressId=" + addressId, e);
					try {
						exporter.rollback();
					} catch (FlexPayException ex) {
						ex.printStackTrace();
						// do nothing
					}
				}
			}
		} catch (Exception e) {
			log.error("Exporter exception", e);
		} finally {
			exporter.endExport();
			lockManager.releaseLock(Resources.BUILDING_ATTRIBUTES);
		}
		log.debug("Tariff calculation result export procces finished");
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
	
	@Required
	public void setSubServiceExportCodes(List<String> subServiceExportCodes) {
		this.subServiceExportCodes = subServiceExportCodes;
	}
}
