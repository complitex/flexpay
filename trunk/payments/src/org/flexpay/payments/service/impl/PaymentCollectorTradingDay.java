package org.flexpay.payments.service.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.process.export.ExportJobParameterNames;
import org.flexpay.payments.service.GeneralizationTradingDay;
import org.flexpay.payments.service.TradingDay;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.DateUtil.*;
import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.PROCESSES_DEFINITION_NAME;

public class PaymentCollectorTradingDay extends GeneralizationTradingDay<PaymentCollector> {

	private static Boolean childProcessDefinitionDeployed = false;
	private TradingDay<PaymentPoint> paymentPointTradingDayService;

	@Override
	public void startTradingDay(@NotNull PaymentCollector paymentCollector) throws FlexPayException {

		if (paymentCollector.getTradingDayProcessInstanceId() != null && isOpened(paymentCollector.getTradingDayProcessInstanceId())) {
			throw new FlexPayException("Trading day for Payment collector '" + paymentCollector.getId() + "' execute");
		}

		deployChildProcessDefinitions();

		Map<Serializable, Serializable> parameters = map();

		Long paymentCollectorId = paymentCollector.getId();
		parameters.put(ExportJobParameterNames.PAYMENT_COLLECTOR_ID, paymentCollectorId);
		log.debug("Set paymentCollectorId {}", paymentCollectorId);

		Date now = now();

		//fill begin and end date
		parameters.put(ExportJobParameterNames.BEGIN_DATE, truncateDay(now));
		log.debug("Set beginDate {}", truncateDay(now));

		parameters.put(ExportJobParameterNames.END_DATE, getEndOfThisDay(now));
		log.debug("Set endDate {}", getEndOfThisDay(now));

		parameters.put(ExportJobParameterNames.ORGANIZATION_ID, paymentCollector.getOrganization().getId());
		log.debug("Set organizationId {}", paymentCollector.getOrganization().getId());

		try {
			String tradingDayProcessDefinitionName = PROCESSES_DEFINITION_NAME.get(PaymentCollector.class.getName());
			long processInstanceId = processManager.createProcess(tradingDayProcessDefinitionName, parameters);
			log.info("'{}' process created. Process instance id = {}, for payment collector with id {}",
					new Object[]{tradingDayProcessDefinitionName, processInstanceId, paymentCollectorId});
		} catch (ProcessInstanceException e) {
			log.error("Failed run process trading day", e);
			throw new FlexPayException(e);
		} catch (ProcessDefinitionException e) {
			log.error("Process trading day not started", e);
			throw new FlexPayException(e);
		}
	}

	@Override
	public void stopTradingDay(@NotNull PaymentCollector paymentCollector) throws FlexPayException {

		for (PaymentPoint paymentPoint : paymentCollector.getPaymentPoints()) {
			paymentPointTradingDayService.stopTradingDay(paymentPoint);
		}
	}

	private void deployChildProcessDefinitions() throws FlexPayException {
		synchronized (PaymentCollectorTradingDay.class) {
			if (childProcessDefinitionDeployed) {
				return;
			}
			childProcessDefinitionDeployed = true;
		}
		deployChildProcessDefinition(Cashbox.class);
		deployChildProcessDefinition(PaymentPoint.class);
	}

	@SuppressWarnings ({"unchecked"})
	private void deployChildProcessDefinition(Class clazz) throws FlexPayException {
		if (!PROCESSES_DEFINITION_NAME.containsKey(clazz.getName())) {
			throw new FlexPayException("Can not find process definition name for class " + clazz.getName());
		}
		try {
			processManager.deployProcessDefinition(PROCESSES_DEFINITION_NAME.get(clazz.getName()), false);
		} catch (ProcessDefinitionException e) {
			throw new FlexPayException(e);
		}
	}

	@Required
	public void setPaymentPointTradingDayService(TradingDay<PaymentPoint> paymentPointTradingDayService) {
		this.paymentPointTradingDayService = paymentPointTradingDayService;
	}
}
