package org.flexpay.payments.actions.monitor.data;

public class PaymentPointMonitorContainer {
    private String id;
    private String name;
    private String paymentsCount;
    private String status;
    private String totalSum;
    private String cashBox;
    private String cashierFIO;
    private String lastPayment;
    private String actionName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentsCount() {
        return paymentsCount;
    }

    public void setPaymentsCount(String paymentsCount) {
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

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
