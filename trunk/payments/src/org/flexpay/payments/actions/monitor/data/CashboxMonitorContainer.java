package org.flexpay.payments.actions.monitor.data;

public class CashboxMonitorContainer {
    private String id;
    private String cashbox;
    private String totalSum;
    private String cashierFIO;
    private String lastPayment;
    private String paymentsCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getPaymentsCount() {
        return paymentsCount;
    }

    public void setPaymentsCount(String paymentsCount) {
        this.paymentsCount = paymentsCount;
    }
}
