package org.flexpay.payments.actions.monitor;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.flexpay.payments.process.export.job.ExportJobParameterNames;
import org.flexpay.payments.service.Roles;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.annotation.Secured;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class PaymentPointEnableDisableAction extends AccountantAWPActionSupport {

	public static final String ENABLE = "enable";
	public static final String DISABLE = "disable";
	private static final String PROCESS_DEFINITION_NAME = "TradingDay";

    private PaymentPoint paymentPoint = new PaymentPoint();
	private String action;

	private ProcessManager processManager;
	private PaymentPointService paymentPointService;
	private PaymentCollectorService paymentCollectorService;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

		if (!(getUserPreferences() instanceof PaymentsUserPreferences)) {
			log.error("{} is not instanceof {}", getUserPreferences().getClass(), PaymentsUserPreferences.class);
			return SUCCESS;
		}

		Long paymentCollectorId = ((PaymentsUserPreferences) getUserPreferences()).getPaymentCollectorId();
		if (paymentCollectorId == null) {
			log.error("PaymentCollectorId is not defined in preferences of User {} (id = {})", getUserPreferences().getUsername(), getUserPreferences().getId());
			return SUCCESS;
		}
		PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));
		if (paymentCollector == null) {
			log.error("No payment collector found with id {}", paymentCollectorId);
			return SUCCESS;
		}

		if (paymentPoint == null || paymentPoint.isNew()) {
			log.warn("Incorrect payment point id {}", paymentPoint);
			return SUCCESS;
		}

		Stub<PaymentPoint> stub = stub(paymentPoint);
		paymentPoint = paymentPointService.read(stub);
		if (paymentPoint == null) {
			log.warn("Can't get Payment point with id {} from DB", stub.getId());
			return SUCCESS;
		} else if (paymentPoint.isNotActive()) {
			log.warn("Payment point with id {} is disabled", stub.getId());
			return SUCCESS;
		}

		for (PaymentPoint point : paymentCollector.getPaymentPoints()) {

			if (stub.getId().equals(point.getId())) {
				if (DISABLE.equals(action) && paymentPoint.getTradingDayProcessInstanceId() != null) {
					disableTradingDay();
					addActionMessage(getText("payments.payment_points.list.disable_trading_day", "Disable trading day for {0} payment point", paymentPoint.getName(getLocale())));
				} else if (ENABLE.equals(action) && paymentPoint.getTradingDayProcessInstanceId() == null) {
					enableTradingDay(paymentCollector);
					addActionMessage(getText("payments.payment_points.list.enable_trading_day", "Enable trading day for {0} payment point", paymentPoint.getName(getLocale())));
				}
				break;
			}
		}

        return SUCCESS;
    }

	@Secured (Roles.TRADING_DAY_ADMIN_ACTION)
	private void enableTradingDay(PaymentCollector paymentCollector) throws Exception {

		log.debug("Try enable trading day for payment point {}", paymentPoint.getId());

		Map<Serializable, Serializable> parameters = map();
		parameters.put(ExportJobParameterNames.PAYMENT_POINT_ID, paymentPoint.getId());
		log.debug("Set paymentPointId {}", paymentPoint.getId());

		//fill begin and end date
		Date beginDate = new Date();
		parameters.put(ExportJobParameterNames.BEGIN_DATE, beginDate);
		log.debug("Set beginDate {}", beginDate);

		Date endDate = DateUtil.getEndOfThisDay(new Date());
		parameters.put(ExportJobParameterNames.END_DATE, endDate);
		log.debug("Set endDate {}", endDate);

		Long recipientOrganizationId = paymentCollector.getOrganization().getId();
		parameters.put(ExportJobParameterNames.ORGANIZATION_ID, recipientOrganizationId);
		log.debug("Set organizationId {}", recipientOrganizationId);

		Long processInstanceId = null;
		try {
			processInstanceId = processManager.createProcess(PROCESS_DEFINITION_NAME, parameters);
			paymentPoint.setTradingDayProcessInstanceId(processInstanceId);
			paymentCollectorService.update(paymentCollector);
			//paymentPointService.update(paymentPoint);
		} catch (ProcessInstanceException e) {
			log.error("Failed run process trading day", e);
			throw new JobExecutionException(e);
		} catch (ProcessDefinitionException e) {
			log.error("Process trading day not started", e);
			throw new JobExecutionException(e);
		} catch (Throwable th) {
			log.error("Payment point was not save", th);
			if (processInstanceId != null) {
				deleteProcess(processInstanceId);
				log.debug("Delete processInstance with id {}", processInstanceId);
			}
		}
	}

	@Secured (Roles.TRADING_DAY_ADMIN_ACTION)
	private void disableTradingDay() throws FlexPayExceptionContainer {
		log.debug("Try disable trading day for payment point {}", paymentPoint.getId());
		try {
			deleteProcess(paymentPoint.getTradingDayProcessInstanceId());
		} catch (Exception e) {
			log.error("Can't delete process \"TradingDay\"", e);
			throw new FlexPayExceptionContainer(new FlexPayException("Can't disable process Trading day", "payments.error.cant_disable_trading_day"));
		}
		paymentPoint.setTradingDayProcessInstanceId(null);
		paymentPointService.update(paymentPoint);
	}

	private void deleteProcess(long processInstanceId) throws Exception {
		Process process = processManager.getProcessInstanceInfo(processInstanceId);
		if (process != null) {
			processManager.deleteProcessInstance(process);
		}
	}

    @NotNull
    @Override
    protected String getErrorResult() {
		return SUCCESS;
    }

	public void setAction(String action) {
		this.action = action;
	}

	public void setPaymentPoint(PaymentPoint paymentPoint) {
		this.paymentPoint = paymentPoint;
	}

	@Required
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}
}
