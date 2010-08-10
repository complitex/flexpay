package org.flexpay.payments.actions.monitor;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.actions.monitor.data.CashboxMonitorContainer;
import org.flexpay.payments.actions.tradingday.TradingDayControlPanel;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.process.export.job.ExportJobParameterNames;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.DateUtil.now;
import static org.flexpay.payments.util.MonitorUtils.*;

public class PaymentPointCashboxesListAction extends AccountantAWPWithPagerActionSupport<CashboxMonitorContainer> implements InitializingBean {

	private PaymentPoint paymentPoint = new PaymentPoint();
	private List<CashboxMonitorContainer> cashboxes = list();

	private TradingDayControlPanel tradingDayControlPanel;

	private ProcessManager processManager;
	private CashboxService cashboxService;
	private OperationService operationService;
	private PaymentPointService paymentPointService;
	private PaymentsStatisticsService paymentsStatisticsService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (paymentPoint == null || paymentPoint.isNew()) {
			log.warn("Incorrect payment point id {}", paymentPoint);
			addActionError(getText("payments.payment_point.detail.error.pp.does_not_set"));
			return ERROR;
		}

		Stub<PaymentPoint> stub = stub(paymentPoint);
		paymentPoint = paymentPointService.read(stub);
		if (paymentPoint == null) {
			log.warn("Can't get Payment point with id {} from DB", stub.getId());
			addActionError(getText("payments.payment_point.detail.error.inner_error"));
			return ERROR;
		} else if (paymentPoint.isNotActive()) {
			log.warn("Payment point with id {} is disabled", stub.getId());
			addActionError(getText("payments.payment_point.detail.error.inner_error"));
			return ERROR;
		}

        PaymentCollector collector = getPaymentCollector();
        if (collector == null) {
            addActionError(getText("payments.error.monitor.cant_get_payment_collector"));
            return SUCCESS;
        }
        Date startDate = now();
        Date finishDate = new Date();

        if (log.isDebugEnabled()) {
            log.debug("Start date={}, finish date={}", formatWithTime(startDate), formatWithTime(finishDate));
        }

        tradingDayControlPanel.updatePanel(collector);

        if (tradingDayControlPanel.isTradingDayOpened()) {

            Process process = processManager.getProcessInstanceInfo(collector.getTradingDayProcessInstanceId());
            if (process == null) {
                log.warn("Can't get trading day process with id {} from DB", collector.getTradingDayProcessInstanceId());
            } else {
                startDate = process.getProcessStartDate();
            }

        }

		List<Cashbox> cbs = cashboxService.findCashboxesForPaymentPoint(paymentPoint.getId());
		for (Cashbox cashbox : cbs) {

			CashboxMonitorContainer container = new CashboxMonitorContainer();
			container.setId(cashbox.getId());
			container.setCashbox(cashbox.getName(getLocale()));

            List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypeCashboxStatistics(stub(cashbox), startDate, finishDate);
			container.setPaymentsCount(getPaymentsCount(statistics));
			container.setTotalSum(String.valueOf(getPaymentsSum(statistics)));

            Operation operation = operationService.getLastPaymentOperationsForCashbox(stub(cashbox), startDate, finishDate);
            if (operation != null) {
                container.setCashierFIO(operation.getCashierFio());
                container.setLastPayment(formatTime.format(operation.getCreationDate()));
            }

			cashboxes.add(container);
		}

		return SUCCESS;
	}

    private Process findTradingDayProcess(PaymentCollector collector, List<Process> processes) {

        for (Process process : processes) {

            Process tradingDayProcess = processManager.getProcessInstanceInfo(process.getId());
            Long paymentCollectorId = (Long) tradingDayProcess.getParameters().get(ExportJobParameterNames.PAYMENT_COLLECTOR_ID);
            log.debug("Closed trading day process paymentCollectorId variable ({}) and this paymentCollector id ({})", paymentCollectorId, collector.getId());

            if (collector.getId().equals(paymentCollectorId)) {
                return tradingDayProcess;
            }
        }

        return null;
    }

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return ERROR;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		tradingDayControlPanel = new TradingDayControlPanel(processManager, AccounterAssignmentHandler.ACCOUNTER, log);
	}

	public List<CashboxMonitorContainer> getCashboxes() {
		return cashboxes;
	}

	public PaymentPoint getPaymentPoint() {
		return paymentPoint;
	}

	public void setPaymentPoint(PaymentPoint paymentPoint) {
		this.paymentPoint = paymentPoint;
	}

	public TradingDayControlPanel getTradingDayControlPanel() {
		return tradingDayControlPanel;
	}

	public void setTradingDayControlPanel(TradingDayControlPanel tradingDayControlPanel) {
		this.tradingDayControlPanel = tradingDayControlPanel;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setPaymentsStatisticsService(PaymentsStatisticsService paymentsStatisticsService) {
		this.paymentsStatisticsService = paymentsStatisticsService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
