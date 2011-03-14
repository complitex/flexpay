package org.flexpay.payments.action.monitor.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.util.PaymentCollectorTradingDayConstants;

public class CashboxMonitorContainer {

    private Long id;
    private String cashbox;
    private String totalSum;
    private String cashierFIO;
    private String lastPayment;
    private Long paymentsCount;
	private PaymentCollectorTradingDayConstants.Status status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCashbox() {
        return cashbox;
    }

    public void setCashbox(String cashbox) {
        this.cashbox = cashbox;
    }

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = totalSum;
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

	public Long getPaymentsCount() {
		return paymentsCount;
	}

	public void setPaymentsCount(Long paymentsCount) {
		this.paymentsCount = paymentsCount;
	}

	public void setStatus(PaymentCollectorTradingDayConstants.Status status) {
		this.status = status;
	}

	public PaymentCollectorTradingDayConstants.Status getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", id).
				append("cashbox", cashbox).
				append("totalSum", totalSum).
				append("cashierFIO", cashierFIO).
				append("lastPayment", lastPayment).
				append("paymentsCount", paymentsCount).
				append("status", status).
				toString();
	}
}
