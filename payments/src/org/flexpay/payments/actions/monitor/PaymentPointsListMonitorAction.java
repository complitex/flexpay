package org.flexpay.payments.actions.monitor;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.process.sorter.ProcessSorterByEndDate;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.actions.monitor.data.PaymentPointMonitorContainer;
import org.flexpay.payments.actions.tradingday.TradingDayControlPanel;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.process.export.TradingDay;
import org.flexpay.payments.process.export.job.ExportJobParameterNames;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.DateUtil.now;
import static org.flexpay.payments.actions.monitor.PaymentPointEnableDisableAction.DISABLE;
import static org.flexpay.payments.actions.monitor.PaymentPointEnableDisableAction.ENABLE;
import static org.flexpay.payments.process.export.TradingDay.PROCESS_STATUS;
import static org.flexpay.payments.process.export.TradingDay.STATUS_CLOSED;
import static org.flexpay.payments.util.MonitorUtils.*;

public class PaymentPointsListMonitorAction extends AccountantAWPWithPagerActionSupport<PaymentPointMonitorContainer> implements InitializingBean {

    private List<PaymentPointMonitorContainer> paymentPoints;

    private TradingDayControlPanel tradingDayControlPanel;

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

//        List<org.flexpay.common.process.Process> processes = processManager.getProcesses(processSorterByName, page, null, null, ProcessState.RUNING, PROCESS_DEFINITION_NAME);
//        List<PaymentPoint> lPaymentPoints = paymentPointService.listPoints(CollectionUtils.arrayStack(), page);
//        for (Process process : processes) {
        for (PaymentPoint paymentPoint : paymentCollector.getPaymentPoints()) {

            PaymentPointMonitorContainer container = new PaymentPointMonitorContainer();

            tradingDayControlPanel.updatePanel(paymentPoint);

            Date startDate = new Date();
            Date finishDate = new Date();

            if (tradingDayControlPanel.isTradingDayOpened()) {

                Process tradingDayProcess = processManager.getProcessInstanceInfo(paymentPoint.getTradingDayProcessInstanceId());
                startDate = tradingDayProcess.getProcessStartDate();

            } else {

                ProcessSorterByEndDate processSorter = new ProcessSorterByEndDate();
                processSorter.setOrder(ObjectSorter.ORDER_DESC);

                List<Process> processes = processManager.getProcesses(processSorter, new Page<Process>(1000), now(), new Date(), ProcessState.COMPLETED, TradingDay.PROCESS_DEFINITION_NAME);
                if (log.isDebugEnabled()) {
                    log.debug("Found {} processes", processes.size());
                }

                if (!processes.isEmpty()) {

                    Process tradingDayProcess = findTradingDayProcess(paymentPoint, processes);
                    log.debug("Closed trading day process: {}", tradingDayProcess);

                    if (tradingDayProcess != null) {
                        startDate = tradingDayProcess.getProcessStartDate();
                        finishDate = tradingDayProcess.getProcessEndDate();
                    }
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("Start date={}, finish date={}", formatWithTime(startDate), formatWithTime(finishDate));
            }

            String status = STATUS_CLOSED;
            if (paymentPoint.getTradingDayProcessInstanceId() != null && paymentPoint.getTradingDayProcessInstanceId() > 0) {
                Process process = processManager.getProcessInstanceInfo(paymentPoint.getTradingDayProcessInstanceId());
                log.debug("Found process for paymentPoint with id {} : {}", paymentPoint.getId(), process);
                status = process != null ? (String) process.getParameters().get(PROCESS_STATUS) : STATUS_CLOSED;
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

    private Process findTradingDayProcess(PaymentPoint paymentPoint, List<Process> processes) {

        for (Process process : processes) {

            Process tradingDayProcess = processManager.getProcessInstanceInfo(process.getId());
            Long paymentPointId = (Long) tradingDayProcess.getParameters().get(ExportJobParameterNames.PAYMENT_POINT_ID);
            log.debug("Closed trading day process paymentPointId variable ({}) and this paymentPoint id ({})", paymentPointId, paymentPoint.getId());

            if (paymentPoint.getId().equals(paymentPointId)) {
                return tradingDayProcess;
            }
        }

        return null;
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        tradingDayControlPanel = new TradingDayControlPanel(processManager, AccounterAssignmentHandler.ACCOUNTER, log);
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
