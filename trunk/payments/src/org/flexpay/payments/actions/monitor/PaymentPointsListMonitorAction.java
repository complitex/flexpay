package org.flexpay.payments.actions.monitor;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.actions.monitor.data.PaymentPointMonitorContainer;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.process.export.TradingDay;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.DateUtil.now;

public class PaymentPointsListMonitorAction extends AccountantAWPWithPagerActionSupport<PaymentPointMonitorContainer> {

    private static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

    private List<PaymentPointMonitorContainer> paymentPoints;

	private ProcessManager processManager;
    private PaymentsStatisticsService paymentsStatisticsService;
    private OperationService operationService;
    private CashboxService cashboxService;

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

		paymentPoints = list();

//        List<org.flexpay.common.process.Process> processes = processManager.getProcesses(processSorterByName, page, null, null, ProcessState.RUNING, PROCESS_DEFINITION_NAME);
//        List<PaymentPoint> lPaymentPoints = paymentPointService.listPoints(CollectionUtils.arrayStack(), page);
//        for (Process process : processes) {
        for (PaymentPoint paymentPoint : paymentCollector.getPaymentPoints()) {
//            paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(paymentPoint));
            PaymentPointMonitorContainer container = new PaymentPointMonitorContainer();

            Date startDate = now();
            Date finishDate = new Date();
            if (log.isDebugEnabled()) {
                log.debug("Start date={} {}, finish date={} {}", new Object[] {format(startDate), formatTime.format(startDate), format(finishDate), formatTime.format(finishDate)});
            }
            
            String status = null;
            if (paymentPoint.getTradingDayProcessInstanceId() != null && paymentPoint.getTradingDayProcessInstanceId() > 0) {
                Process process = processManager.getProcessInstanceInfo(paymentPoint.getTradingDayProcessInstanceId());
                if (process != null) {
                    status = (String) process.getParameters().get(TradingDay.PROCESS_STATUS);
                    // PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(pointId));
                }
            }
            List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypePaymentPointStatistics(stub(paymentPoint), startDate, finishDate);
            List<Operation> operations = operationService.listLastPaymentOperationsForPaymentPoint(stub(paymentPoint), startDate, finishDate);

            container.setId(paymentPoint.getId());
            container.setName(paymentPoint.getName(getLocale()));
            container.setPaymentsCount(getPaymentsCount(statistics));
            container.setTotalSumm(String.valueOf(getPaymentsSumm(statistics)));
            container.setStatus(status);

            if (paymentPoint.getTradingDayProcessInstanceId() != null) {
                container.setAction(PaymentPointEnableDisableAction.DISABLE);
            } else {
                container.setAction(PaymentPointEnableDisableAction.ENABLE);
            }

            if (operations != null && !operations.isEmpty()) {
                Operation operation = operations.get(0);
                Cashbox operationCashbox = cashboxService.read(stub(operation.getCashbox()));
                if (operationCashbox != null) {
					container.setCashierFIO(operation.getCashierFio());
                    container.setCashBox(operationCashbox.getName(getLocale()));
                    container.setLastPayment(formatTime.format(operation.getCreationDate()));
                }
            }

            paymentPoints.add(container);
        }

        return SUCCESS;
    }


    public Long getPaymentsCount(List<OperationTypeStatistics> typeStatisticses) {
        Long count = 0L;
        for (OperationTypeStatistics stats : typeStatisticses) {
            if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
                count += stats.getCount();
            }
        }
        return count;
    }

    private BigDecimal getPaymentsSumm(List<OperationTypeStatistics> typeStatisticses) {
        BigDecimal sum = BigDecimal.ZERO;
        for (OperationTypeStatistics stats : typeStatisticses) {
            if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
                sum = sum.add(stats.getSumm());
            }
        }
        return sum;
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
    public void setCashboxService(CashboxService cashboxService) {
        this.cashboxService = cashboxService;
    }

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
