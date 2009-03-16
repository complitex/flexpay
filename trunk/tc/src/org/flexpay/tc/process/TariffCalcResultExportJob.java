package org.flexpay.tc.process;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.importexport.imp.ClassToTypeRegistry;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.tc.locking.Resources;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.process.exporters.Exporter;
import org.flexpay.tc.service.TariffCalculationResultService;

import org.springframework.beans.factory.annotation.Required;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TariffCalcResultExportJob extends Job {

	private LockManager lockManager;
	private TariffCalculationResultService tariffCalculationResultService;
	private Exporter exporter;
	public final static String CALCULATION_DATE = "CALCULATION_DATE";
	public final static String PERIOD_BEGIN_DATE = "PERIOD_BEGIN_DATE";
	private List<String> subServiceExportCodes;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		log.debug("Tariff calculation result export procces started");

		if (!lockManager.lock(Resources.BUILDING_ATTRIBUTES)) {
			log.info("Another process has already requested a lock and is working");
			return RESULT_NEXT;
		}

		try {
			exporter.beginExport();

			Date calculationDate = (Date) parameters.get(CALCULATION_DATE);
			Date periodBeginDate = (Date) parameters.get(PERIOD_BEGIN_DATE);

			List<Long> addressIds = tariffCalculationResultService.getAddressIds(calculationDate);


			for (Long addressId : addressIds) {
				List<TariffCalculationResult> tariffCalcResultList = tariffCalculationResultService.getTariffCalcResultsByCalcDateAndAddressId(calculationDate, new Stub<BuildingAddress>(addressId));
				try {
					for (String subServiceCode : subServiceExportCodes){
						TariffCalculationResult tcr = getTariffCalculationResultBySubserviceCode(tariffCalcResultList, subServiceCode);
						if (tcr == null){
							tcr = new TariffCalculationResult();
							Tariff tariff = new Tariff(); tariff.setSubServiceCode(subServiceCode);
							Building building = new Building();
							building.setId(addressId);
							tcr.setBuilding(building);
							tcr.setTariff(tariff);
							tcr.setCalculationDate(calculationDate);
							tcr.setValue(BigDecimal.ZERO);
						}
						exporter.export(new Object[]{tcr, periodBeginDate});
					}
					exporter.commit();
				} catch (FlexPayException  e) {
					log.error("SQL error for addressId=" + addressId, e);
					try {
						exporter.rollback();
					} catch (FlexPayException ex) {
						ex.printStackTrace();
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

	private TariffCalculationResult getTariffCalculationResultBySubserviceCode(@NotNull List<TariffCalculationResult> tcrl, @NotNull String subserviceCode){
		for (TariffCalculationResult tcr : tcrl){
			if (subserviceCode.equals(tcr.getTariff().getSubServiceCode())){
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

}
