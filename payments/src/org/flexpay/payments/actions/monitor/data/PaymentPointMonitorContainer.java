package org.flexpay.payments.actions.monitor.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.util.PaymentCollectorTradingDayConstants;

public class PaymentPointMonitorContainer {

    private Long id;
    private String name;
    private Long paymentsCount;
    private PaymentCollectorTradingDayConstants.Status status;
    private String totalSum;
    private String cashbox;
    private String cashierFIO;
    private String lastPayment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPaymentsCount() {
		return paymentsCount;
	}

	public void setPaymentsCount(Long paymentsCount) {
		this.paymentsCount = paymentsCount;
	}

	public PaymentCollectorTradingDayConstants.Status getStatus() {
		return status;
	}

	public void setStatus(PaymentCollectorTradingDayConstants.Status status) {
		this.status = status;
	}

	public String getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(String totalSum) {
		this.totalSum = totalSum;
	}

	public String getCashbox() {
		return cashbox;
	}

	public void setCashbox(String cashbox) {
		this.cashbox = cashbox;
	}

	public String getCashierFIO() {
		return cashierFIO;
	}

	public void setCashierFIO(String cashierFIO) {
		this.cashierFIO = cashierFIO;
	}

	public String getLastPayment() {
		return lastPayment;
	}

	public void setLastPayment(String lastPayment) {
		this.lastPayment = lastPayment;
	}

    @Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", id).
				append("name", name).
				append("paymentsCount", paymentsCount).
				append("status", status).
				append("totalSum", totalSum).
				append("cashbox", cashbox).
				append("cashierFIO", cashierFIO).
				append("lastPayment", lastPayment).
				toString();
	}

}
