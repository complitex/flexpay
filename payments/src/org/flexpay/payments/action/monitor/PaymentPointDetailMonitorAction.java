package org.flexpay.payments.action.monitor;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.util.MonitorUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.payments.util.MonitorUtils.formatTime;
import static org.flexpay.payments.util.MonitorUtils.getPaymentsSum;

public class PaymentPointDetailMonitorAction extends AccountantAWPActionSupport {
    
	private PaymentPoint paymentPoint = new PaymentPoint();
    private Long paymentsCount;
    private String totalSum;
	private String updated;

    private ProcessManager processManager;
    private PaymentsStatisticsService paymentsStatisticsService;
    private PaymentPointService paymentPointService;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

		if (paymentPoint == null || paymentPoint.isNew()) {
			log.warn("Incorrect payment point id {}", paymentPoint);
			addActionError(getText("payments.payment_point.detail.error.pp.does_not_set"));
			return REDIRECT_ERROR;
		}

		Stub<PaymentPoint> stub = stub(paymentPoint);
		paymentPoint = paymentPointService.read(stub);
		if (paymentPoint == null) {
			log.warn("Can't get Payment point with id {} from DB", stub.getId());
			addActionError(getText("payments.payment_point.detail.error.inner_error"));
			return REDIRECT_ERROR;
		} else if (paymentPoint.isNotActive()) {
			log.warn("Payment point with id {} is disabled", stub.getId());
			addActionError(getText("payments.payment_point.detail.error.inner_error"));
			return REDIRECT_ERROR;
		}

        Date startDate = new Date();
        Date finishDate = new Date();

        if (paymentPoint.getTradingDayProcessInstanceId() != null) {

            ProcessInstance tradingDayProcess = processManager.getProcessInstance(paymentPoint.getTradingDayProcessInstanceId());
            if (tradingDayProcess != null) {
                startDate = tradingDayProcess.getStartDate();
                if (tradingDayProcess.getEndDate() != null) {
                    finishDate = tradingDayProcess.getEndDate();
                }
            }

        }

        if (log.isDebugEnabled()) {
            log.debug("Start date={}, finish date={}", formatWithTime(startDate), formatWithTime(finishDate));
        }

		List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypePaymentPointStatistics(stub, startDate, finishDate);
		paymentsCount = MonitorUtils.getPaymentsCount(statistics);
		totalSum = getPaymentsSum(statistics).toString();

        updated = formatTime.format(new Date());

        return SUCCESS;
    }

	@NotNull
    @Override
    protected String getErrorResult() {
        return REDIRECT_ERROR;
    }

    public Long getPaymentsCount() {
        return paymentsCount;
    }

	public PaymentPoint getPaymentPoint() {
		return paymentPoint;
	}

	public void setPaymentPoint(PaymentPoint paymentPoint) {
		this.paymentPoint = paymentPoint;
	}

	public String getTotalSum() {
        return totalSum;
    }

    public String getUpdated() {
        return updated;
    }

    @Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
        this.paymentPointService = paymentPointService;
	}

    @Required
    public void setPaymentsStatisticsService(PaymentsStatisticsService paymentsStatisticsService) {
        this.paymentsStatisticsService = paymentsStatisticsService;
    }

    @Required
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

}
