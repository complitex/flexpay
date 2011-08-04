package org.flexpay.payments.service.impl;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.process.export.ExportJobParameterNames;
import org.flexpay.payments.service.GeneralizationTradingDay;
import org.flexpay.payments.service.TradingDay;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.DateUtil.*;
import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.PROCESSES_DEFINITION_NAME;

public class PaymentCollectorTradingDay extends GeneralizationTradingDay<PaymentCollector> {

	private static Boolean childProcessDefinitionDeployed = false;
	private TradingDay<PaymentPoint> paymentPointTradingDayService;
	private Date defaultBeginTime;
	private Date defaultEndTime;

	@Override
	public boolean startTradingDay(@NotNull PaymentCollector paymentCollector) throws FlexPayException {

		if (paymentCollector.getTradingDayProcessInstanceId() != null && isOpened(paymentCollector.getTradingDayProcessInstanceId())) {
			log.warn("Trading day for Payment collector '{}' execute", paymentCollector.getId());
			return false;
		}

		deployChildProcessDefinitions();

		Map<String, Object> parameters = map();

		Long paymentCollectorId = paymentCollector.getId();
		parameters.put(ExportJobParameterNames.PAYMENT_COLLECTOR_ID, paymentCollectorId);
		log.debug("Set {} {}", ExportJobParameterNames.PAYMENT_COLLECTOR_ID, paymentCollectorId);

		Date now = now();

		//fill begin and end date
		parameters.put(ExportJobParameterNames.BEGIN_DATE, truncateDay(now));
		log.debug("Set {} {}", ExportJobParameterNames.BEGIN_DATE, truncateDay(now));

		parameters.put(ExportJobParameterNames.END_DATE, getEndOfThisDay(now));
		log.debug("Set {} {}", ExportJobParameterNames.END_DATE, getEndOfThisDay(now));

		parameters.put(ExportJobParameterNames.ORGANIZATION_ID, paymentCollector.getOrganization().getId());
		log.debug("Set {} {}", ExportJobParameterNames.ORGANIZATION_ID, paymentCollector.getOrganization().getId());

		//set begin date
		Date tradingDayBeginTime;
		if (paymentCollector.getTradingDayBeginTime() != null) {
			tradingDayBeginTime = paymentCollector.getTradingDayBeginTime();
		} else {
			tradingDayBeginTime = defaultBeginTime;
		}

		Date tradingDayBeginDate = setTimeAtDate(now, tradingDayBeginTime);
		parameters.put(ExportJobParameterNames.TRADING_DAY_BEGIN_DATE, tradingDayBeginDate);
		log.debug("Set {} {}", ExportJobParameterNames.TRADING_DAY_BEGIN_DATE, tradingDayBeginDate);

		//set end date
		Date tradingDayEndTime;
		if (paymentCollector.getTradingDayEndTime() != null) {
			tradingDayEndTime = paymentCollector.getTradingDayEndTime();
		} else {
			tradingDayEndTime = defaultEndTime;
		}

		Date tradingDayEndDate = setTimeAtDate(now, tradingDayEndTime);
		parameters.put(ExportJobParameterNames.TRADING_DAY_END_DATE, tradingDayEndDate);
		log.debug("Set {} {}", ExportJobParameterNames.TRADING_DAY_END_DATE, tradingDayEndDate);

		try {
			String tradingDayProcessDefinitionName = PROCESSES_DEFINITION_NAME.get(PaymentCollector.class.getName());
			ProcessInstance processInstance = processManager.startProcess(tradingDayProcessDefinitionName, parameters);
			log.info("'{}' process created. ProcessInstance instance id = {}, for payment collector with id {}",
					new Object[]{tradingDayProcessDefinitionName, processInstance, paymentCollectorId});
		} catch (ProcessInstanceException e) {
			log.error("Failed run process trading day", e);
			throw new FlexPayException(e);
		} catch (ProcessDefinitionException e) {
			log.error("ProcessInstance trading day not started", e);
			throw new FlexPayException(e);
		}
		return true;
	}

	private Date setTimeAtDate(Date now, Date tradingDayEndTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(tradingDayEndTime);

		Date tradingDayEndDay = DateUtils.addHours(DateUtil.truncateDay(now), calendar.get(Calendar.HOUR_OF_DAY));
		tradingDayEndDay      = DateUtils.addMinutes(tradingDayEndDay, calendar.get(Calendar.MINUTE));
		return tradingDayEndDay;
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
		/*
		if (!PROCESSES_DEFINITION_NAME.containsKey(clazz.getName())) {
			throw new FlexPayException("Can not find process definition name for class " + clazz.getName());
		}
		try {
			processManager.deployProcessDefinition(PROCESSES_DEFINITION_NAME.get(clazz.getName()), false);
		} catch (ProcessDefinitionException e) {
			throw new FlexPayException(e);
		}
		*/
	}

	@Required
	public void setPaymentPointTradingDayService(TradingDay<PaymentPoint> paymentPointTradingDayService) {
		this.paymentPointTradingDayService = paymentPointTradingDayService;
	}

	@Required
	public void setDefaultBeginTime(Date defaultBeginTime) {
		this.defaultBeginTime = defaultBeginTime;
	}

	@Required
	public void setDefaultEndTime(Date defaultEndTime) {
		this.defaultEndTime = defaultEndTime;
	}
}
