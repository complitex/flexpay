package org.flexpay.payments.actions.monitor;

import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.actions.monitor.data.PaymentPointMonitorContainer;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.process.export.TradingDay;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.DateUtil.now;
import static org.flexpay.payments.actions.monitor.MonitorUtils.*;
import static org.flexpay.payments.actions.monitor.PaymentPointEnableDisableAction.DISABLE;
import static org.flexpay.payments.actions.monitor.PaymentPointEnableDisableAction.ENABLE;

public class PaymentPointsListMonitorAction extends AccountantAWPWithPagerActionSupport<PaymentPointMonitorContainer> {

    private List<PaymentPointMonitorContainer> paymentPoints;

	private ProcessManager processManager;
    private PaymentsStatisticsService paymentsStatisticsService;
    private OperationService operationService;

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

//        List<org.flexpay.common.process.Process> processes = processManager.getProcesses(processSorterByName, page, null, null, ProcessState.RUNING, PROCESS_DEFINITION_NAME);
//        List<PaymentPoint> lPaymentPoints = paymentPointService.listPoints(CollectionUtils.arrayStack(), page);
//        for (Process process : processes) {
        for (PaymentPoint paymentPoint : paymentCollector.getPaymentPoints()) {

            PaymentPointMonitorContainer container = new PaymentPointMonitorContainer();

            Date startDate = now();
            Date finishDate = new Date();
            if (log.isDebugEnabled()) {
                log.debug("Start date={}, finish date={}", formatWithTime(startDate), formatWithTime(finishDate));
            }
            
            String status = null;
            if (paymentPoint.getTradingDayProcessInstanceId() != null && paymentPoint.getTradingDayProcessInstanceId() > 0) {
                Process process = processManager.getProcessInstanceInfo(paymentPoint.getTradingDayProcessInstanceId());
                log.debug("Found process for paymentPoint with id {} : {}", paymentPoint.getId(), process);
                if (process != null) {
                    status = (String) process.getParameters().get(TradingDay.PROCESS_STATUS);
                }
            }
            container.setId(paymentPoint.getId());
            container.setName(paymentPoint.getName(getLocale()));
            container.setStatus(status);
            container.setAction(paymentPoint.getTradingDayProcessInstanceId() == null ? ENABLE : DISABLE);

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
