package org.flexpay.payments.actions.monitor;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.flexpay.payments.process.export.TradingDay;
import org.flexpay.payments.process.export.job.ExportJobParameterNames;
import org.flexpay.payments.service.Roles;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.annotation.Secured;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.DateUtil.getEndOfThisDay;

public class PaymentPointEnableDisableAction extends AccountantAWPActionSupport {

	public static final String ENABLE = "enable";
	public static final String DISABLE = "disable";

	private String action;

	private ProcessManager processManager;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

        PaymentCollector collector = getPaymentCollector();
        if (collector == null) {
            return SUCCESS;
        }

        Long tradingDayId = collector.getTradingDayProcessInstanceId();
        Process process = null;
        if (tradingDayId != null) {
            process = processManager.getProcessInstanceInfo(tradingDayId);
        }

        log.debug("action = {}, tradingDayId = {}, process = {}", new Object[] {action, tradingDayId, process});

        if (DISABLE.equals(action) && tradingDayId != null) {
            disableTradingDay(collector);
            addActionMessage(getText("payments.payment_points.list.disable_trading_day", "Disable trading day for {0} payment collector", collector.getName(getLocale())));
        } else if (ENABLE.equals(action) && (tradingDayId == null || process == null)) {
            log.debug("Trying create new trading day process instance");
            enableTradingDay(collector);
            addActionMessage(getText("payments.payment_points.list.enable_trading_day", "Enable trading day for {0} payment collector", collector.getName(getLocale())));
        }

        return SUCCESS;
    }

	@Secured (Roles.TRADING_DAY_ADMIN_ACTION)
	private void enableTradingDay(PaymentCollector collector) throws Exception {

		log.debug("Try enable trading day for payment collector {}", collector.getId());

		Map<Serializable, Serializable> parameters = map();
		parameters.put(ExportJobParameterNames.PAYMENT_COLLECTOR_ID, collector.getId());
		log.debug("Set paymentCollectorId {}", collector.getId());

		//fill begin and end date
		Date beginDate = new Date();
		parameters.put(ExportJobParameterNames.BEGIN_DATE, beginDate);
		log.debug("Set beginDate {}", beginDate);

		Date endDate = getEndOfThisDay(new Date());
		parameters.put(ExportJobParameterNames.END_DATE, endDate);
		log.debug("Set endDate {}", endDate);

		Long recipientOrganizationId = collector.getOrganization().getId();
		parameters.put(ExportJobParameterNames.ORGANIZATION_ID, recipientOrganizationId);
		log.debug("Set organizationId {}", recipientOrganizationId);

		Long processInstanceId = null;
		try {
			processInstanceId = processManager.createProcess(TradingDay.PROCESS_DEFINITION_NAME, parameters);
			collector.setTradingDayProcessInstanceId(processInstanceId);
            paymentCollectorService.update(collector);
		} catch (ProcessInstanceException e) {
			log.error("Failed run process trading day", e);
			throw new JobExecutionException(e);
		} catch (ProcessDefinitionException e) {
			log.error("Process trading day not started", e);
			throw new JobExecutionException(e);
		} catch (Throwable th) {
			log.error("Payment collector was not save", th);
			if (processInstanceId != null) {
				deleteProcess(processInstanceId);
				log.debug("Delete processInstance with id {}", processInstanceId);
			}
		}
	}

	@SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Secured (Roles.TRADING_DAY_ADMIN_ACTION)
	private void disableTradingDay(PaymentCollector collector) throws FlexPayExceptionContainer {

		log.debug("Try disable trading day for payment collector {}", collector.getId());
		try {
			deleteProcess(collector.getTradingDayProcessInstanceId());
		} catch (Exception e) {
			log.error("Can't delete process \"TradingDay\"", e);
			throw new FlexPayExceptionContainer(new FlexPayException("Can't disable process Trading day", "payments.error.cant_disable_trading_day"));
		}
		collector.setTradingDayProcessInstanceId(null);
		paymentCollectorService.update(collector);
	}

	private void deleteProcess(Long processInstanceId) throws Exception {
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

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
