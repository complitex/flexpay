package org.flexpay.payments.actions.monitor;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.DateUtil.now;

public class PaymentPointDetailMonitorAction extends AccountantAWPActionSupport {
    
    public static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

	private PaymentPoint paymentPoint = new PaymentPoint();
    private Long paymentsCount;
    private String totalSumm;
	private String updated;

    private PaymentPointService paymentPointService;
    private PaymentsStatisticsService paymentsStatisticsService;

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

		Date startDate = now();
		Date finishDate = new Date();

		List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypePaymentPointStatistics(stub, startDate, finishDate);
		paymentsCount = getPaymentsCount(statistics);
		totalSumm = String.valueOf(getPaymentsSumm(statistics));

        updated = formatTime.format(new Date());

        return SUCCESS;
    }    

    public static Long getPaymentsCount(List<OperationTypeStatistics> typeStatisticses) {
		Long count = 0L;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
				count += stats.getCount();
			}
		}
		return count;
	}

	public static BigDecimal getPaymentsSumm(List<OperationTypeStatistics> typeStatisticses) {
		BigDecimal summ = new BigDecimal("0.00");
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
				summ = summ.add(stats.getSumm());
			}
		}
		return summ;
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

	public String getTotalSumm() {
        return totalSumm;
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

}
