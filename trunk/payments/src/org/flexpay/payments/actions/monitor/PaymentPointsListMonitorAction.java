package org.flexpay.payments.actions.monitor;

import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.actions.monitor.data.PaymentPointMonitorContainer;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.util.PaymentCollectorTradingDayConstants;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.actions.monitor.PaymentCollectorEnableDisableAction.DISABLE;
import static org.flexpay.payments.actions.monitor.PaymentCollectorEnableDisableAction.ENABLE;
import static org.flexpay.payments.util.MonitorUtils.*;
import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.PROCESS_STATUS;
import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.Statuses;

public class PaymentPointsListMonitorAction extends AccountantAWPWithPagerActionSupport<PaymentPointMonitorContainer> {

    private List<PaymentPointMonitorContainer> paymentPoints;
    private Statuses processStatus = Statuses.CLOSED;

    private ProcessManager processManager;
    private OperationService operationService;
    private PaymentsStatisticsService paymentsStatisticsService;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

        if (!(getUserPreferences() instanceof PaymentsUserPreferences)) {
            log.error("{} is not instanceof {}", getUserPreferences().getClass(), PaymentsUserPreferences.class);
            addActionError(getText("payments.error.monitor.internal_error"));
            return SUCCESS;
        }

        PaymentCollector paymentCollector = getPaymentCollector();
        if (paymentCollector == null) {
            addActionError(getText("payments.error.monitor.cant_get_payment_collector"));
            return SUCCESS;
        }

		paymentPoints = list();

        for (PaymentPoint paymentPoint : paymentCollector.getPaymentPoints()) {

            PaymentPointMonitorContainer container = new PaymentPointMonitorContainer();

            Date startDate = new Date();
            Date finishDate = new Date();
            Process tradingDayProcess = null;

            if (paymentPoint.getTradingDayProcessInstanceId() != null) {
				tradingDayProcess = processManager.getProcessInstanceInfo(paymentPoint.getTradingDayProcessInstanceId());
                log.debug("Found process for paymentPoint with id {} : {}", paymentPoint.getId(), tradingDayProcess);
			}

            if (tradingDayProcess != null) {
                startDate = tradingDayProcess.getProcessStartDate();
                if (tradingDayProcess.getProcessEndDate() != null) {
                    finishDate = tradingDayProcess.getProcessEndDate();
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("Start date={}, finish date={}", formatWithTime(startDate), formatWithTime(finishDate));
            }

            Statuses status = Statuses.CLOSED;
            if (tradingDayProcess != null) {
                status = (Statuses) tradingDayProcess.getParameters().get(PROCESS_STATUS);
            }
            container.setId(paymentPoint.getId());
            container.setName(paymentPoint.getName(getLocale()));
            container.setStatus(status);
            container.setAction(tradingDayProcess == null ? ENABLE : DISABLE);

            List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypePaymentPointStatistics(stub(paymentPoint), startDate, finishDate);
            container.setPaymentsCount(getPaymentsCount(statistics));
            container.setTotalSum(String.valueOf(getPaymentsSum(statistics)));

            Operation operation = operationService.getLastPaymentOperationForPaymentPoint(stub(paymentPoint), startDate, finishDate);
            if (operation != null) {
                container.setCashierFIO(operation.getCashierFio());
                container.setCashBox(operation.getCashbox().getName(getLocale()));
                container.setLastPayment(formatTime.format(operation.getCreationDate()));
            }

            paymentPoints.add(container);
        }

        Date startDate = new Date();
        Date finishDate = new Date();

        if (paymentCollector.getTradingDayProcessInstanceId() != null) {

            Process tradingDayProcess = processManager.getProcessInstanceInfo(paymentCollector.getTradingDayProcessInstanceId());
            if (tradingDayProcess != null) {
                startDate = tradingDayProcess.getProcessStartDate();
                if (tradingDayProcess.getProcessEndDate() != null) {
                    finishDate = tradingDayProcess.getProcessEndDate();
                }
                processStatus = (Statuses) tradingDayProcess.getParameters().get(PROCESS_STATUS);
            }

        }

        if (log.isDebugEnabled()) {
            log.debug("Start date={}, finish date={}", formatWithTime(startDate), formatWithTime(finishDate));
        }

        return SUCCESS;
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    public List<PaymentPointMonitorContainer> getPaymentPoints() {
        return paymentPoints;
    }

    public PaymentCollectorTradingDayConstants.Statuses getProcessStatus() {
        return processStatus;
    }

    public boolean isOpen() {
        return Statuses.OPEN.equals(processStatus);
    }

    @Required
    public void setPaymentsStatisticsService(PaymentsStatisticsService paymentsStatisticsService) {
        this.paymentsStatisticsService = paymentsStatisticsService;
    }

    @Required
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
