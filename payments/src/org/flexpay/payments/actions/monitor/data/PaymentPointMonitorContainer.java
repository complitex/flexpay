package org.flexpay.payments.actions.monitor.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PaymentPointMonitorContainer {

    private Long id;
    private String name;
    private Long paymentsCount;
    private String status;
    private String totalSum;
    private String cashBox;
    private String cashierFIO;
    private String lastPayment;
    private String action;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(String totalSum) {
		this.totalSum = totalSum;
	}

	public String getCashBox() {
		return cashBox;
	}

	public void setCashBox(String cashBox) {
		this.cashBox = cashBox;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", id).
				append("name", name).
				append("paymentsCount", paymentsCount).
				append("status", status).
				append("totalSum", totalSum).
				append("cashBox", cashBox).
				append("cashierFIO", cashierFIO).
				append("lastPayment", lastPayment).
				append("action", action).
				toString();
	}
}
