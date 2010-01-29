package org.flexpay.payments.actions.monitor.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CashboxMonitorContainer {

    private Long id;
    private String cashbox;
    private String totalSumm;
    private String cashierFIO;
    private String lastPayment;
    private Long paymentsCount;

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

    public String getTotalSumm() {
        return totalSumm;
    }

    public void setTotalSumm(String totalSumm) {
        this.totalSumm = totalSumm;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", id).
				append("cashbox", cashbox).
				append("totalSumm", totalSumm).
				append("cashierFIO", cashierFIO).
				append("lastPayment", lastPayment).
				append("paymentsCount", paymentsCount).
				toString();
	}
}
