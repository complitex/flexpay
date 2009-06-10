package org.flexpay.payments.actions.monitor;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.process.sorter.ProcessSorterByEndDate;
import org.flexpay.common.process.sorter.ProcessSorterByName;
import org.flexpay.common.process.sorter.ProcessSorterByStartDate;
import org.flexpay.common.process.sorter.ProcessSorterByState;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.CashboxCookieWithPagerActionSupport;
import org.flexpay.payments.actions.monitor.data.PaymentPointMonitorContainer;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.time.DateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class PaymentPointsListMonitorAction extends CashboxCookieWithPagerActionSupport<PaymentPointMonitorContainer> {
    private static final String PROCESS_DEFINITION_NAME = "TradingDay";

    private static final SimpleDateFormat formatTimeUpdated = new SimpleDateFormat("HH:mm");

    // sorters
	private static final ProcessSorterByName processSorterByName = new ProcessSorterByName();
	private static final ProcessSorterByStartDate processSorterByStartDate = new ProcessSorterByStartDate();
	private static final ProcessSorterByEndDate processSorterByEndDate = new ProcessSorterByEndDate();
	private static final ProcessSorterByState processSorterByState = new ProcessSorterByState();

    private String selectedPaymentPointName;
    private String filter;
    private String updated;
    private String update;
    private String detail;
    private List<PaymentPointMonitorContainer> paymentPoints;

    private ProcessManager processManager;
    private PaymentsStatisticsService paymentsStatisticsService;
    private PaymentPointService paymentPointService;

    /**
	 * {@inheritDoc}
	 */
    @NotNull
    protected String doExecute() throws Exception {
        Page<org.flexpay.common.process.Process> page = new Page<org.flexpay.common.process.Process>();
        page.setPageSize(getPageSize());
        page.setPageNumber(getPager().getPageNumber());

        List<org.flexpay.common.process.Process> processes = processManager.getProcesses(processSorterByName, page, null, null, ProcessState.RUNING, PROCESS_DEFINITION_NAME);

        paymentPoints = new ArrayList<PaymentPointMonitorContainer>();
        for (Process process : processes) {
            process = processManager.getProcessInstanceInfo(process.getId());
            Long pointId = (Long) process.getParameters().get("paymentPointId");
            String status = (String) process.getParameters().get("PROCESS_STATUS");
		    PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(pointId));
            if (paymentPoint == null) {
                log.error("Payment point with id - {} does not exist", pointId);
                return ERROR;
            }
            Date endDate = DateUtil.now();
            Date startDate = DateUtils.setHours(endDate, 0);
            startDate = DateUtils.setMinutes(startDate, 0);
            startDate = DateUtils.setSeconds(startDate, 0);
            List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypePaymentPointStatistics(Stub.stub(paymentPoint), startDate, endDate);

            PaymentPointMonitorContainer container = new PaymentPointMonitorContainer();
            container.setId(String.valueOf(process.getId()));
            container.setName(paymentPoint.getName(getLocale()));
            container.setPaymentsCount(String.valueOf(getPaymentsCount(statistics)));
            container.setTotalSum(String.valueOf(getPaymentsSumm(statistics)));
            container.setStatus(status);

            container.setCashBox(null);
            container.setCashierFIO(null);
            container.setLastPayment(null);

            paymentPoints.add(container);
        }

        getPager().setElements(paymentPoints);
        getPager().setTotalElements(page.getTotalNumberOfElements());

        updated = formatTimeUpdated.format(new Date());
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
