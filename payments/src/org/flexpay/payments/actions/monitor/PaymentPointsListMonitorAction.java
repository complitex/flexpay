package org.flexpay.payments.actions.monitor;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.actions.CashboxCookieWithPagerActionSupport;
import org.flexpay.payments.actions.monitor.data.PaymentPointMonitorContainer;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.process.export.TradingDay;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Set;
import java.text.SimpleDateFormat;

public class PaymentPointsListMonitorAction extends FPActionWithPagerSupport<PaymentPointMonitorContainer> {
//    private static final String PROCESS_DEFINITION_NAME = "TradingDay";

    private static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

    private String selectedPaymentPointName;
    private String filter;
    private String updated;
    private String update;
    private String detail;
    private List<PaymentPointMonitorContainer> paymentPoints;

    private ProcessManager processManager;
    private PaymentsStatisticsService paymentsStatisticsService;
    private PaymentPointService paymentPointService;
    private OperationService operationService;
    private CashboxService cashboxService;
    private PaymentsCollectorService paymentsCollectorService;

    /**
     * {@inheritDoc}
     */
    @NotNull
    protected String doExecute() throws Exception {
        Page<PaymentPoint> page = new Page<PaymentPoint>();
        page.setPageSize(getPageSize());
        page.setPageNumber(getPager().getPageNumber());

//        List<org.flexpay.common.process.Process> processes = processManager.getProcesses(processSorterByName, page, null, null, ProcessState.RUNING, PROCESS_DEFINITION_NAME);

		paymentPoints = new ArrayList<PaymentPointMonitorContainer>();
		Long paymentCollectorId = ((PaymentsUserPreferences) getUserPreferences()).getPaymentCollectorId();
		PaymentsCollector paymentsCollector = paymentsCollectorService.read(new Stub<PaymentsCollector>(paymentCollectorId));
		if (paymentsCollector == null) {
			log.error("No payment collector found with id {}", paymentCollectorId);
			return ERROR;
		}

        Set<PaymentPoint> lPaymentPoints = paymentsCollector.getPaymentPoints();
//        List<PaymentPoint> lPaymentPoints = paymentPointService.listPoints(CollectionUtils.arrayStack(), page);
//        for (Process process : processes) {
        for (PaymentPoint paymentPoint : lPaymentPoints) {
            paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(paymentPoint));

            Date startDate = DateUtil.now();
            Date finishDate = new Date();
            log.debug("Start date={} {}, finish date={} {}", new Object[]{DateUtil.format(startDate), formatTime.format(startDate), DateUtil.format(finishDate), formatTime.format(finishDate)});
            
            String status = null;
            if (paymentPoint.getTradingDayProcessInstanceId() != null && paymentPoint.getTradingDayProcessInstanceId() > 0) {
                Process process = processManager.getProcessInstanceInfo(paymentPoint.getTradingDayProcessInstanceId());
                if (process != null) {
                    status = (String) process.getParameters().get(TradingDay.PROCESS_STATUS);
                    // PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(pointId));
                }
            }
            List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypePaymentPointStatistics(Stub.stub(paymentPoint), startDate, finishDate);
            List<Operation> operations = operationService.listLastPaymentOperations(paymentPoint, startDate, finishDate);

            PaymentPointMonitorContainer container = new PaymentPointMonitorContainer();
            container.setId(String.valueOf(paymentPoint.getId()));
            container.setName(paymentPoint.getName(getLocale()));
            container.setPaymentsCount(String.valueOf(getPaymentsCount(statistics)));
            container.setTotalSum(String.valueOf(getPaymentsSumm(statistics)));
            container.setStatus(status);

            if (operations != null && operations.size() > 0) {
                Operation operation = operations.get(0);
                Cashbox operationCashbox = cashboxService.read(new Stub<Cashbox>(operation.getCashbox()));
                if (operationCashbox != null) {
					container.setCashierFIO(operation.getCashierFio());
                    container.setCashBox(operationCashbox.getName(getLocale()));
                    container.setLastPayment(formatTime.format(operation.getCreationDate()));
                }
            }

            paymentPoints.add(container);
        }

        getPager().setElements(paymentPoints);
        getPager().setTotalElements(page.getTotalNumberOfElements());

        updated = formatTime.format(new Date());
        return SUCCESS;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    protected String getErrorResult() {
        return SUCCESS;
    }

    public String getSelectedPaymentPointName() {
        return selectedPaymentPointName;
    }

    public void setSelectedPaymentPointName(String selectedPaymentPointName) {
        this.selectedPaymentPointName = selectedPaymentPointName;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<PaymentPointMonitorContainer> getPaymentPoints() {
        return paymentPoints;
    }

    public void setPaymentPoints(List<PaymentPointMonitorContainer> paymentPoints) {
        this.paymentPoints = paymentPoints;
    }

    @Required
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
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
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

    @Required
    public void setCashboxService(CashboxService cashboxService) {
        this.cashboxService = cashboxService;
    }

    @Required
    public void setPaymentsCollectorService(PaymentsCollectorService paymentsCollectorService) {
        this.paymentsCollectorService = paymentsCollectorService;
    }

    public long getPaymentsCount(List<OperationTypeStatistics> typeStatisticses) {
        long count = 0;
        for (OperationTypeStatistics stats : typeStatisticses) {
            if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
                count += stats.getCount();
            }
        }
        return count;
    }

    public BigDecimal getPaymentsSumm(List<OperationTypeStatistics> typeStatisticses) {
        BigDecimal summ = BigDecimal.ZERO;
        for (OperationTypeStatistics stats : typeStatisticses) {
            if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
                summ = summ.add(stats.getSumm());
            }
        }
        return summ;
    }
}
